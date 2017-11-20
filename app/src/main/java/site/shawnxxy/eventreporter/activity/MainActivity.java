package site.shawnxxy.eventreporter.activity;

//import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import site.shawnxxy.eventreporter.utils.AlertDialogManager;
import site.shawnxxy.eventreporter.R;
import site.shawnxxy.eventreporter.constructor.User;
import site.shawnxxy.eventreporter.utils.SessionManager;
import site.shawnxxy.eventreporter.utils.Utils;
//import site.shawnxxy.eventreporter.fragments.EventFragment;

public class MainActivity extends AppCompatActivity {

    // to show multiple fragments in one activity
//    private EventFragment mListFragment;
//    private CommentFragment mGridFragment;

    //  For Login Activity
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mSubmitButton;
    private Button mRegisterButton;
    private DatabaseReference mDatabase;
    // Shared Preferences
    private SessionManager session;
    // Alert Dialog Manager
    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("Life cycle test", "We are at onCreate()"); // TEST in log

        /**
         *  For Sessions using Shared Preferences
         */
        session = new SessionManager(getApplicationContext());

        /**
         *  For Firebase
         */
        // Firebase uses singleton to initialize the sdk
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsernameEditText = (EditText) findViewById(R.id.editTextLogin);
        mPasswordEditText = (EditText) findViewById(R.id.editTextPassword);
        mSubmitButton = (Button) findViewById(R.id.submit);
        mRegisterButton = (Button) findViewById(R.id.register);
        //admob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//        myRef.setValue("Hello, World!");

        /**
         *  Implements button registration click event
         */
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsernameEditText.getText().toString();
                final String password = Utils.md5Encryption(mPasswordEditText.getText().toString());
                final User user = new User(username, password, System.currentTimeMillis());
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (username.trim().length() > 0 && password.trim().length() > 0) {
                            if (dataSnapshot.hasChild(username)) {
                                alert.showAlertDialog(MainActivity.this, "Registration failed..", "Username is already registered, please change one.", false);
//                                Toast.makeText(getBaseContext(),"Username is already registered, please change one", Toast.LENGTH_SHORT).show();
                            } else if (!username.equals("") && !password.equals("")) {
                                // put username as key to set value
                                mDatabase.child("users").child(user.getUsername()).setValue(user);
                                Toast.makeText(getBaseContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            alert.showAlertDialog(MainActivity.this, "Registration failed..", "Please input valid username or password and try again!", false);
//                            Toast.makeText(getBaseContext(),"Please input valid username or password and try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }); // END of setOnClickListener()

        /**
         *  Implements login click event
         */
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsernameEditText.getText().toString();
                final String password = Utils.md5Encryption(mPasswordEditText.getText().toString());
                // Check firebase data
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (username.trim().length() > 0 && password.trim().length() > 0) {
                            if (dataSnapshot.hasChild(username) && (password.equals(dataSnapshot.child(username).child("password").getValue()))) {
                                Log.i("Your log", "You successfully login");
                                session.createLoginSession(username, password);
                                Intent myIntent = new Intent(MainActivity.this, EventActivity.class);
                                Utils.username = username;
                                startActivity(myIntent);
                                finish();
                            } else {
                                alert.showAlertDialog(MainActivity.this, "Login failed..", "Incorrect Username/Password", false);
//                                Toast.makeText(getBaseContext(),"Incorrect username or password. Please try again or register.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            alert.showAlertDialog(MainActivity.this, "Login failed..", "Please input valid username or password and try again!", false);
//                            Toast.makeText(getBaseContext(),"Please input valid username or password and try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        /**
         *  show multiple fragments in one activity
         */
//        //add list view
//        mListFragment = new EventFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.event_container, mListFragment).commit();
//        //add Gridview
//        if (isTablet()) {
//            mGridFragment = new CommentFragment();
//            getSupportFragmentManager().beginTransaction().add(R.id.comment_container, mGridFragment).commit();
//        }
//        //Show different fragments based on screen size.
//        if (findViewById(R.id.fragment_container) != null) {
//            Fragment fragment = isTablet() ? new CommentFragment() : new EventFragment();
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
//        }

        /**
         *  Below section is not needed for Fragment
         */
//        // Get ListView object from xml.
//        ListView eventListView = (ListView) findViewById(R.id.event_list);
//        // Initialize an adapter.
//        EventAdapter adapter = new EventAdapter(this);
//        // Assign adapter to ListView.
//        eventListView.setAdapter(adapter);
//        // Initialize an adapter. Used for the dummy function test below
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,
//                R.layout.event_item,
//                R.id.event_name,
//                getEventNames()
//        );

    } // END OF onCreate()

//    private boolean isTablet() {
//        return (getApplicationContext().getResources().getConfiguration().screenLayout &
//                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
//    }

//    @Override
//    public void onItemSelected(int position) { // defined in CommentFragment
//        mGridFragment.onItemSelected(position);
//    }
//
//    @Override
//    public void onCommentSelected(int position) {
//        mListFragment.onItemSelected(position);
//    }

    /**
     * A dummy function to get fake event names.
     *
     * @return an array of fake event names.
     */
//    private String[] getEventNames() {
//        String[] names = {
//                "Event1", "Event2", "Event3",
//                "Event4", "Event5", "Event6",
//                "Event7", "Event8", "Event9",
//                "Event10", "Event11", "Event12"};
//        return names;
//    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Life cycle test", "We are at onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Life cycle test", "We are at onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Life cycle test", "We are at onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Life cycle test", "We are at onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Life cycle test", "We are at onDestroy()");
    }


}
