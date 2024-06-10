package com.group2.project.jsonmanipulator;

import com.group2.project.calendaractivity.CalendarActivity;
import com.group2.project.calendarobjects.Event;
import com.group2.project.calendarobjects.Meeting;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class RawJSONWriter
{

    // Class defines a JSON translator+parser for writing to an external JSON file.

    private final static String jsonDir = "src/main/docker/data-postgres/data-postgres.json";

    // Static function that takes the map of Calendar Objects and dates to write to the JSON file.
    public static void writeJSON(Map<Integer, Map<Integer, Map<Integer, List<CalendarActivity>>>> toWrite)
    {
        try {

            JSONArray finalizedArray = parseJSON(toWrite);

            //System.out.println("To Write: " + finalizedArray.toJSONString());

            // ! WARNING : calling new FileWriter() even without writing will overwrite existing file !
            FileWriter writer = new FileWriter(jsonDir);
            writer.write(finalizedArray.toJSONString());
            writer.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method that iterates through the map and translates values into appropriate JSON format.
    private static JSONArray parseJSON(Map<Integer, Map<Integer, Map<Integer, List<CalendarActivity>>>> toParse)
    {
        JSONArray toReturn = new JSONArray();

        // Iterate through all the year keys:
        for(Integer yearKey : toParse.keySet())
        {
            JSONObject yearObj = new JSONObject();
            yearObj.put("year", yearKey.toString());
            JSONArray monthsArray = new JSONArray(); // key : "months"

            // Iterate through all the month keys:
            for(Integer monthKey : toParse.get(yearKey).keySet())
            {
                JSONObject monthObj = new JSONObject();
                monthObj.put("month", monthKey.toString());
                JSONArray daysArray = new JSONArray(); // key : "days"

                // Iterate through all the day keys:
                for(Integer dayKey : toParse.get(yearKey).get(monthKey).keySet())
                {
                    JSONObject dayObj = new JSONObject();
                    dayObj.put("day", dayKey.toString());
                    JSONArray eventsArray = new JSONArray();

                    // Iterate through the list that contains CalendarActivity (higher level representations of CalendarObject(s))
                    for(CalendarActivity currentActivity : toParse.get(yearKey).get(monthKey).get(dayKey))
                    {
                        // parse CalendarActivity --> JSONObject
                        // calls on helper method to parse values from the calendar object to a JSON object
                        JSONObject parsed = parseCalendarActivity(currentActivity);
                        eventsArray.add(parsed);
                    }
                    if(!eventsArray.isEmpty())
                    {
                        dayObj.put("events", eventsArray);
                        daysArray.add(dayObj);
                    }
                }
                if(!daysArray.isEmpty())
                {
                    monthObj.put("days", daysArray);
                    monthsArray.add(monthObj);
                }
            }
            if(!monthsArray.isEmpty())
            {
                yearObj.put("months", monthsArray);
                toReturn.add(yearObj);
            }
        }

        return toReturn;
    }

    // helper-method that gets values from the runtime type of the CalendarObject and respectively fills in values for that representation of a JSON object.
    private static JSONObject parseCalendarActivity(CalendarActivity activity)
    {
        JSONObject toReturn = new JSONObject();

        switch (activity.getType())
        {
            case CALENDAR_OBJECT -> {
                toReturn.put("type", "CalendarObject");
                toReturn.put("title", activity.getCalendarObject().getTitle());
                toReturn.put("desc", activity.getCalendarObject().getDescription());
            }
            // Note... downcasting is dangerous, but using [instanceof] comparison along with development insight makes it seamless & effective!
            case EVENT -> {
                if(activity.getCalendarObject() instanceof Event)
                {
                    Event currentEvent;
                    currentEvent = (Event) activity.getCalendarObject();
                    toReturn.put("type", "Event");
                    toReturn.put("title", currentEvent.getTitle());
                    toReturn.put("desc", currentEvent.getDescription());
                    toReturn.put("time", currentEvent.getTime());
                }
            }
            case MEETING -> {
                if(activity.getCalendarObject() instanceof Meeting)
                {
                    Meeting currentMeeting;
                    currentMeeting = (Meeting) activity.getCalendarObject();
                    toReturn.put("type", "Meeting");
                    toReturn.put("title", currentMeeting.getTitle());
                    toReturn.put("desc", currentMeeting.getDescription());
                    toReturn.put("time", currentMeeting.getTime());
                    toReturn.put("loc", currentMeeting.getLocation());
                }
            }
        }

        return toReturn;
    }

}
