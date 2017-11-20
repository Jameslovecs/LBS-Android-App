package site.shawnxxy.eventreporter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import site.shawnxxy.eventreporter.utils.DataService;
import site.shawnxxy.eventreporter.constructor.Event;
import site.shawnxxy.eventreporter.R;

/**
 * Created by ShawnX on 8/27/17.
 */

public class EventAdapter extends BaseAdapter {

    Context context;
    List<Event> eventData;

    public EventAdapter(Context context) {
        this.context = context;
        eventData = DataService.getEventData();
    }

    @Override
    public int getCount() {
        return eventData.size();
    }

    @Override
    public Event getItem(int position) {
        return eventData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_item, parent, false);
        }

        TextView eventTitle = (TextView) convertView.findViewById(R.id.event_title);
        TextView eventAddress = (TextView) convertView.findViewById(R.id.event_address);
        TextView eventDescription = (TextView) convertView.findViewById(R.id.event_description);
//        ImageView eventImage = (ImageView) convertView.findViewById(R.id.event_thumbnail);
        Event event = eventData.get(position);
        eventTitle.setText(event.getTitle());
        eventAddress.setText(event.getAddress());
        eventDescription.setText(event.getDescription());

        // Communicate back from GridView to ListView
        ImageView eventImage = (ImageView) convertView.findViewById(R.id.event_thumbnail);
        //match iamges with different title
//        if (position < 2) {
//            eventImage.setImageDrawable(context.getDrawable(R.drawable.event_thumbnail));
//        } else if (position >= 2 && position < 4) {
//            eventImage.setImageDrawable(context.getDrawable(R.drawable.banana));
//        } else if (position >= 4 && position < 6) {
//            eventImage.setImageDrawable(context.getDrawable(R.drawable.orange));
//        } else {
//            eventImage.setImageDrawable(context.getDrawable(R.drawable.pear));
//        }
        return convertView;
    }

}
