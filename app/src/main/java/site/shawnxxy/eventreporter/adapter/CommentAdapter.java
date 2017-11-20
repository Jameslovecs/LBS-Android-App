package site.shawnxxy.eventreporter.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import site.shawnxxy.eventreporter.constructor.Comment;
import site.shawnxxy.eventreporter.constructor.Event;
import site.shawnxxy.eventreporter.R;
import site.shawnxxy.eventreporter.utils.Utils;
import site.shawnxxy.eventreporter.activity.CommentActivity;

import static site.shawnxxy.eventreporter.utils.Utils.timeTransformer;

/**
 * Created by ShawnX on 9/17/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private final static int TYPE_EVENT = 0;
    private final static int TYPE_COMMENT = 1;

    private List<Comment> commentList;
    private Event event;

    private DatabaseReference databaseReference;
    private LayoutInflater inflater;

    public CommentAdapter(Context context) {
        this.context = context;
        commentList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setEvent(final Event event) {
        this.event = event;
    }

    public void setComments(final List<Comment> comments) {
        this.commentList = comments;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_EVENT : TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        return commentList.size() + 1;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView eventUser;
        public TextView eventTitle;
        public TextView eventLocation;
        public TextView eventDescription;
        public TextView eventTime;
        public ImageView eventImgView;
        public ImageButton btnLike;
        public ImageButton btnComment;
//        public ImageView eventImgViewRepost;

        public TextView eventLikeNumber;
//        public TextView eventCommentNumber;
//        public TextView eventRepostNumber;
        public View layout;

        public EventViewHolder(View v) {
            super(v);
            layout = v;
            eventUser = (TextView) v.findViewById(R.id.comment_main_user);
            eventTitle = (TextView) v.findViewById(R.id.comment_main_title);
            eventLocation = (TextView) v.findViewById(R.id.comment_main_location);
            eventDescription = (TextView) v.findViewById(R.id.comment_main_description);
            eventTime = (TextView) v.findViewById(R.id.comment_main_time);
            eventImgView = (ImageView) v.findViewById(R.id.comment_main_image);
            btnLike = (ImageButton) v.findViewById(R.id.btnLike);
            btnComment = (ImageButton) v.findViewById(R.id.btnComment);
//            eventImgViewRepost = (ImageView) v.findViewById(R.id.comment_main_repost_img);
            eventLikeNumber = (TextView) v.findViewById(R.id.comment_main_like_number);
//            eventCommentNumber = (TextView) v.findViewById(R.id.comment_main_comment_number);
//            eventRepostNumber = (TextView) v.findViewById(R.id.comment_main_repost_number);
        }
    } // End

    /**
     *  create another holder that holds comment item
     */
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentUser;
        public TextView commentTime;
        public TextView commentDescription;
        public View layout;
        public CommentViewHolder(View v) {
            super(v);
            layout = v;
            commentUser = (TextView) v.findViewById(R.id.comment_item_user);
            commentTime = (TextView) v.findViewById(R.id.comment_item_time);
            commentDescription = (TextView) v.findViewById(R.id.comment_item_description);
        }
    }

    /**
     *  create a view for holders
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case TYPE_EVENT:
                v = inflater.inflate(R.layout.comment_main, parent, false);
                viewHolder = new EventViewHolder(v);
                break;
            case TYPE_COMMENT:
                v = inflater.inflate(R.layout.comment_item, parent, false);
                viewHolder = new CommentViewHolder(v);
                break;
        }
        return viewHolder;
    }

    /**
     *   shows view in recycler view
     * @param holder
     * @param position
     */
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_EVENT:
                EventViewHolder viewHolderEvent = (EventViewHolder) holder;
                configureEventView(viewHolderEvent);
                break;
            case TYPE_COMMENT:
                CommentViewHolder viewHolderAds = (CommentViewHolder) holder;
                configureCommentView(viewHolderAds, position);
                break;
        }
    }

    private void configureCommentView(final CommentViewHolder holder, int position) {
        final Comment comment = commentList.get(position - 1);
        holder.commentUser.setText(comment.getCommenter());
        holder.commentTime.setText(Utils.timeTransformer(comment.getTime()));
        holder.commentDescription.setText(comment.getDescription());
    }

    boolean isLike = false;
    private void configureEventView(final EventViewHolder holder) {
        holder.eventUser.setText(event.getUsername());
        holder.eventTitle.setText(event.getTitle());
        String[] locations = event.getAddress().split(",");
        holder.eventLocation.setText(locations[1] + "," + locations[2]);
        holder.eventDescription.setText(event.getDescription());
        holder.eventTime.setText(timeTransformer(event.getTime()));
//        holder.eventCommentNumber.setText(String.valueOf(event.getCommentNumber()));
        holder.eventLikeNumber.setText(String.valueOf(event.getLike()));
//        holder.eventRepostNumber.setText(String.valueOf(event.getRepostNumber()));

        if (event.getImgUri() != null) {
            final String url = event.getImgUri();
            holder.eventImgView.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Bitmap>(){
                @Override
                protected Bitmap doInBackground(Void... params) {
                    return Utils.getBitmapFromURL(url);
                }
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    holder.eventImgView.setImageBitmap(bitmap);
                }
            }.execute();
        } else {
            holder.eventImgView.setVisibility(View.GONE);
        }
        // Add click event listener to Like
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLike) {
                    holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
                    isLike = false;
                } else {
                    holder.btnLike.setImageResource(R.drawable.ic_heart_red);
                    isLike = true;
                }
                databaseReference.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Event recordedevent = snapshot.getValue(Event.class);
                            if (recordedevent.getId().equals(event.getId())) {
                                int number = recordedevent.getLike();
                                holder.eventLikeNumber.setText(String.valueOf(number + 1));
                                snapshot.getRef().child("like").setValue(number + 1);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        // Comments Activity Intent
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                String eventId = event.getId();
                intent.putExtra("EventID", eventId);
                context.startActivity(intent);
            }
        });
    }

}
