package site.shawnxxy.eventreporter.utils;

import java.util.ArrayList;
import java.util.List;

import site.shawnxxy.eventreporter.constructor.Event;

/**
 * Created by ShawnX on 8/27/17.
 */

public class DataService {
    /**
     * Fake all the event data for now. We will refine this and connect
     * to our backend later.
     */
    public static List<Event> getEventData() {
        List<Event> eventData = new ArrayList<Event>();
        for (int i = 0; i < 10; ++i) {
            eventData.add(
                    new Event("Event", "1184 W valley Blvd, CA 90101", "This is a huge event"));
        }
        return eventData;
    }

}
