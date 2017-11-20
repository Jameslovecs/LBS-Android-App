//package site.shawnxxy.eventreporter.fragments;
//
//
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
////import android.app.Fragment;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import site.shawnxxy.eventreporter.R;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class EventFragment extends Fragment {
//
//    public EventFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     *  Let MainActivity be able to communicate with CommentFragment
//     */
//    OnItemSelectListener mCallback;
//    // Container Activity must implement this interface
//    public interface OnItemSelectListener {
//        public void onItemSelected(int position);
//    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mCallback = (OnItemSelectListener) context;
//        } catch (ClassCastException e) {
//            //do something
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.fragment_event, container, false);
//        View view = inflater.inflate(R.layout.fragment_event, container, false);
//        ListView listView = (ListView) view.findViewById(R.id.event_list);
////        listView.setAdapter(new EventAdapter(getActivity())); // Comment this off if operating multiple fragments
//        // For multiple fragments in one activity
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                getActivity(),
//                android.R.layout.simple_list_item_1,
//                getEventNames()
//        );
//        //Assign adapter to ListView
//        listView.setAdapter(adapter);
//        // Add click listener in listfragment to let MainActivity be able to communicate with CommentFragment
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                mCallback.onItemSelected(i);
//            }
//        });
//        return view;
//    }
//
//    private String[] getEventNames() {
//        String[] names = {
//                "Event1", "Event2", "Event3",
//                "Event4", "Event5", "Event6",
//                "Event7", "Event8", "Event9",
//                "Event10", "Event11", "Event12"
//        };
//        return names;
//    }
//
//    /**
//     *  communicate back from GridView to ListView
//     */
//    ListView listView;
//    public void onItemSelected(int position){
//        for (int i = 0; i < listView.getChildCount(); i++){
//            if (position == i) {
//                listView.getChildAt(i).setBackgroundColor(Color.BLUE);
//            } else {
//                listView.getChildAt(i).setBackgroundColor(Color.parseColor("#EEEEEE"));
//            }
//        }
//    }
//
//
//}
