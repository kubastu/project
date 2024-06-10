package com.group2.project.headless;

import com.group2.project.calendarobjects.CalendarObject;

import java.util.ArrayList;
import java.util.Date;

public class CalendarManager
{

    // JZ: this class defines a static singleton manager class for the basic Calendar controller.
    //     This controller features simple Add, Remove, and List functions for Calendar I/O.

    private static ArrayList<CalendarObject> calendarObjects = new ArrayList<>();

    // JZ: Add / Remove functions return boolean, similar to ArrayList add/remove behavior.
    public static boolean addCalendarObject(CalendarObject toAdd)
    {
        return(calendarObjects.add(toAdd));
    }

    public static boolean removeCalendarObject(CalendarObject toRemove)
    {
        return(calendarObjects.remove(toRemove));
    }

    // JZ: Listing of calendar objects is overloaded, allows for displaying of calendarObjects by date!
    public static String listCalendarObjects()
    {
        return(calendarObjects.toString());
    }

    public static String listCalendarObjects(Date byDate)
    {
        String toReturn = "";
        for(int i = 0; i < calendarObjects.size(); i++)
        {
            if(calendarObjects.get(i).getDate().equals(byDate))
            {
                toReturn = toReturn + calendarObjects.get(i) + "\n";
            }
        }

        return(toReturn);
    }

}
