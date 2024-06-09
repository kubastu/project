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

    private final static String jsonDir = "src/main/docker/data-postgres/data-postgres.json";

    public static void writeJSON(Map<Integer, Map<Integer, Map<Integer, List<CalendarActivity>>>> toWrite)
    {
        try {
            //FileWriter writer = new FileWriter(jsonDir);
            // writer.write(jsonObj.toString) ++ writer.close()

            JSONArray finalizedArray = parseJSON(toWrite);
            if(finalizedArray == null)
            {
                // todo: how to handle this case?
            }
            else
            {
                System.out.println("To Write: " + finalizedArray.toJSONString());
                // ! WARNING : calling new FileWriter() even without writing will overwrite existing file !
                FileWriter writer = new FileWriter(jsonDir);
                writer.write(finalizedArray.toJSONString());
                writer.close();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONArray parseJSON(Map<Integer, Map<Integer, Map<Integer, List<CalendarActivity>>>> toParse)
    {
        JSONArray toReturn = new JSONArray();

        for(Integer yearKey : toParse.keySet())
        {
            JSONObject yearObj = new JSONObject();
            yearObj.put("year", yearKey.toString());
            JSONArray monthsArray = new JSONArray(); // key : "months"
            for(Integer monthKey : toParse.get(yearKey).keySet())
            {
                JSONObject monthObj = new JSONObject();
                monthObj.put("month", monthKey.toString());
                JSONArray daysArray = new JSONArray(); // key : "days"
                for(Integer dayKey : toParse.get(yearKey).get(monthKey).keySet())
                {
                    JSONObject dayObj = new JSONObject();
                    dayObj.put("day", dayKey.toString());
                    JSONArray eventsArray = new JSONArray();
                    for(CalendarActivity currentActivity : toParse.get(yearKey).get(monthKey).get(dayKey))
                    {
                        // parse CalendarActivity --> JSONObject
                        JSONObject parsed = parseCalendarActivity(currentActivity);
                        eventsArray.add(parsed);
                    }
                    dayObj.put("events", eventsArray);
                    daysArray.add(dayObj);
                }
                monthObj.put("days", daysArray);
                monthsArray.add(monthObj);
            }
            yearObj.put("months", monthsArray);
            toReturn.add(yearObj);
        }

        return toReturn;
    }

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
