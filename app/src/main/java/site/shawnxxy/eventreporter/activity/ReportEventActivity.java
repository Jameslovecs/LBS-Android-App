package site.shawnxxy.eventreporter.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import site.shawnxxy.eventreporter.utils.AlertDialogManager;
import site.shawnxxy.eventreporter.constructor.Event;
import site.shawnxxy.eventreporter.utils.LocationTracker;
import site.shawnxxy.eventreporter.R;
import site.shawnxxy.eventreporter.utils.Utils;

public class ReportEventActivity extends AppCompatActivity {

    /**
     *  To gather user information and push to firebase database, create and initialize UI widgets
     */
    private static final String TAG = ReportEventActivity.class.getSimpleName();
    private EditText mEditTextLocation;
    private EditText mEditTextTitle;
    private EditText mEditTextContent;
    private ImageView mImageViewSend;
    private ImageView mImageViewCamera;
    private DatabaseReference database;
    // Add Firebase Authentification
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // track location
    private LocationTracker mLocationTracker;
    private Activity mActivity;
    // Add image preview
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView img_event_picture;
    private Uri mImgUri;
    // Upload connected pictures to Firebase Storage
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ImageView mImageViewLocation;
    // Alert Dialog Manager
    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_event);

        mEditTextLocation = (EditText) findViewById(R.id.edit_text_event_location);
        mEditTextTitle = (EditText) findViewById(R.id.edit_text_event_title);
        mEditTextContent = (EditText) findViewById(R.id.edit_text_event_content);
        mImageViewCamera = (ImageView) findViewById(R.id.img_event_camera);
        mImageViewSend = (ImageView) findViewById(R.id.img_event_report);
        database = FirebaseDatabase.getInstance().getReference();
        // Add click event to camera icon, trigger event to choose pictures
        mImageViewSend.setOnClickListener(new View.OnClickListener() {
            final String title = mEditTextTitle.getText().toString();
            final String content = mEditTextContent.getText().toString();
            @Override
            public void onClick(View v) {
                String key = uploadEvent(); // function defined in below
//                if (title.trim().length() > 0 && content.trim().length() > 0) {
                    if (mImgUri != null) {
                        uploadImage(key);
                        mImgUri = null;
                    }
//                } else {
//                    alert.showAlertDialog(ReportEventActivity.this, "Post failed..", "Title or Content cannot be empty!", false);
//                }
            }
        });

        /**
         *  Add image preview
         */
        img_event_picture = (ImageView) findViewById(R.id.img_event_picture_capture); // add image preview
        mImageViewLocation = (ImageView) findViewById(R.id.img_event_location);
        // upload picture
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        /**
         *  Add authentication to monitor the sign in status and allow anonymous signing method available for Firebase
         */
        //auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        mAuth.signInAnonymously().addOnCompleteListener(this,  new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInAnonymously", task.getException());
                }
            }
        });

        /**
         *  Check if GPS enabled
         */
        mActivity = this;
        mLocationTracker = new LocationTracker(mActivity);
        mLocationTracker.getLocation();
        final double latitude = mLocationTracker.getLatitude();
        final double longitude = mLocationTracker.getLongitude();
        Log.i("dddddddd", latitude + ":" + longitude); // TEST
        mImageViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {
                    private List<String> mAddressList = new ArrayList<>();
                    @Override
                    protected Void doInBackground(Void... urls) {
                        mAddressList = mLocationTracker.getCurrentLocationViaJSON(latitude,longitude);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void input) {
                        if (mAddressList.size() >= 3) {
                            mEditTextLocation.setText(mAddressList.get(0) + ", " + mAddressList.get(1) + ", " + mAddressList.get(2) + ", " + mAddressList.get(3));
                        }
                    }
                }.execute();
            }
        });

        // image preview
        mImageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

    } // END of onCreate()

    /**
     *  Helper function
     * @return uploadEvent()
     * @return uploadImage()
     */
    private String uploadEvent() {
        String title = mEditTextTitle.getText().toString();
        String location = mEditTextLocation.getText().toString();
        String description = mEditTextContent.getText().toString();
        if (location.equals("") || description.equals("") ||
                title.equals("") || Utils.username == null) {
            return null;
        }
        //create event instance
        Event event = new Event();
        event.setTitle(title);
        event.setAddress(location);
        event.setDescription(description);
        event.setTime(System.currentTimeMillis());
        event.setUsername(Utils.username);
        String key = database.child("events").push().getKey();
        event.setId(key);
        database.child("events").child(key).setValue(event, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    alert.showAlertDialog(ReportEventActivity.this, "Post failed..", "Please check your network status!", false);
//                    Toast toast = Toast.makeText(getBaseContext(),
//                            "Failed to post! Please check your network status.", Toast.LENGTH_SHORT);
//                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getBaseContext(), "Successfully posted!", Toast.LENGTH_SHORT);
                    toast.show();
                    mEditTextTitle.setText("");
                    mEditTextLocation.setText("");
                    mEditTextContent.setText("");
                    Intent intent = new Intent(ReportEventActivity.this, EventActivity.class);
                    startActivity(intent);
                }
            }
        });
        return key;
    }
    private void uploadImage(final String eventId) {
        if (mImgUri == null) {
            return;
        }
        StorageReference imgRef = storageRef.child("images/" + mImgUri.getLastPathSegment() + "_" + System.currentTimeMillis());
        UploadTask uploadTask = imgRef.putFile(mImgUri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                alert.showAlertDialog(ReportEventActivity.this, "Something goes wrong...", "Please try again!", false);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.i(TAG, "upload successfully" + eventId);
                database.child("events").child(eventId).child("imgUri").setValue(downloadUrl.toString());
            }
        });
    }

    /**
     *  Add and remove auth listeners
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     *  Allow to go to another activity to choose a picture and return back to current activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                img_event_picture.setVisibility(View.VISIBLE);
                img_event_picture.setImageURI(selectedImage);
                mImgUri = selectedImage;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
