package com.group2.project.jsonmanipulator;

import com.group2.project.calendaractivity.CalendarActivity;
import com.group2.project.calendaractivity.CalendarController;
import com.group2.project.calendarobjects.CalendarObject;
import com.group2.project.calendarobjects.CalendarType;
import com.group2.project.calendarobjects.Event;
import com.group2.project.calendarobjects.Meeting;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawJSONParser
{

    // Class defines a JSON reader+parser for a local JSON file.

    private final static String jsonDir = "src/main/docker/data-postgres/data-postgres.json";

    // Static function that is called that takes a Java Class to send results back to.
    public static void readJSON(CalendarController calendarController)
    {
        try {
            // todo what happens if file / directory is null?
            //InputStream is = JSONReader.class.getResourceAsStream("src/main/docker/data-postgres/data-postgres.json");
            File jsonFile = new File(jsonDir);

            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));

            // Attempts to construct a String from the content of the JSON file:
            StringBuilder result = new StringBuilder();
            for(String line; (line = reader.readLine()) != null;)
            {
                result.append(line);
            }

            //System.out.println(result.toString());

            // Uses secondary library to parse the built string into an actual JSON Array.
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(result.toString());

            System.out.println(arr.toJSONString());

            // Calls on helper-function to appropriately parse the JSON and send it back to CalendarController::acceptMap()
            parseJSON(calendarController, arr);

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    // Helper method that takes in a Java Class to send results to, and the pre-parsed JSON Array.
    private static void parseJSON(CalendarController calendarController, JSONArray jsonArray)
    {
        try {

            ZonedDateTime temp = ZonedDateTime.now();
            Map<Integer, Map<Integer, Map<Integer, List<CalendarActivity>>>> toReturn = new HashMap<>();

            // Iterate through all the years:
            for(int x = 0; x < jsonArray.size(); x++)
            {
                JSONObject currentYear = (JSONObject) jsonArray.get(x);
                String yearId = (String) currentYear.get("year");
                JSONArray monthsArray = (JSONArray) currentYear.get("months");

                // Iterate through all the months:
                for(int y = 0; y < monthsArray.size(); y++)
                {
                    JSONObject currentMonth = (JSONObject) monthsArray.get(y);
                    String monthId = (String) currentMonth.get("month");
                    JSONArray daysArray = (JSONArray) currentMonth.get("days");

                    // Iterate through all the days:
                    for(int z = 0; z < daysArray.size(); z++)
                    {
                        JSONObject currentDay = (JSONObject) daysArray.get(z);
                        String dayId = (String) currentDay.get("day");
                        JSONArray eventsArray = (JSONArray) currentDay.get("events");

                        // Iterate through all the events on a specific date:
                        for (int i = 0; i < eventsArray.size(); i++)
                        {
                            JSONObject currentEvent = (JSONObject) eventsArray.get(i);

                            int year, month, day;
                            year = Integer.parseInt(yearId);
                            month = Integer.parseInt(monthId);
                            day = Integer.parseInt(dayId);
                            System.out.println("Parsing: " + year + "-" + month + "-" + day);

                            ZonedDateTime dateTime = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, temp.getZone());

                            // calls on helper-function to take the JSON Object and parse it into a respective CalendarObject, Event, or Meeting
                            CalendarActivity currentParsed = parseIndivActivity(currentEvent, dateTime);

                            // duplicated version of CalendarController::addToMap():
                            if(currentParsed != null)
                            {
                                if(toReturn.get(year) == null)
                                {
                                    toReturn.put(year, new HashMap<>());
                                    toReturn.get(year).put(month, new HashMap<>());
                                    toReturn.get(year).get(month).put(day, new ArrayList<>());
                                    toReturn.get(year).get(month).get(day).add(currentParsed);
                                }
                                else
                                {
                                    if(toReturn.get(year).get(month) == null)
                                    {
                                        toReturn.get(year).put(month, new HashMap<>());
                                        toReturn.get(year).get(month).put(day, new ArrayList<>());
                                        toReturn.get(year).get(month).get(day).add(currentParsed);
                                    }
                                    else
                                    {
                                        if(toReturn.get(year).get(month).get(day) == null)
                                        {
                                            toReturn.get(year).get(month).put(day, new ArrayList<>());
                                            toReturn.get(year).get(month).get(day).add(currentParsed);
                                        }
                                        else
                                        {
                                            toReturn.get(year).get(month).get(day).add(currentParsed);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Then... send the results back to the Calling-Java class.
            calendarController.acceptMap(toReturn);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Helper method that takes a JSONObject and constructs an appropriate CalendarActivity based on the runtime type of it.
    public static CalendarActivity parseIndivActivity(JSONObject obj, ZonedDateTime date)
    {
        CalendarActivity toReturn = null;

        String type = (String) obj.get("type");
        switch (type)
        {
            case "CalendarObject" -> {
                String title = (String) obj.get("title");
                String desc = (String) obj.get("desc");
                toReturn = new CalendarActivity(CalendarType.CALENDAR_OBJECT, date, new CalendarObject(date, title, desc));
            }
            case "Event" -> {
                String title = (String) obj.get("title");
                String desc = (String) obj.get("desc");
                String time = (String) obj.get("time");
                toReturn = new CalendarActivity(CalendarType.EVENT, date, new Event(date, title, desc, time));
            }
            case "Meeting" -> {
                String title = (String) obj.get("title");
                String desc = (String) obj.get("desc");
                String time = (String) obj.get("time");
                String location = (String) obj.get("loc");
                toReturn = new CalendarActivity(CalendarType.MEETING, date, new Meeting(date, title, desc, time, location));
            }
        }

        System.out.println("Parsed: " + toReturn.getCalendarObject());

        return toReturn;
    }

}
