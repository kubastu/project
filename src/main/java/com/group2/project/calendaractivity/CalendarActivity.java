package com.group2.project.calendaractivity;

import com.group2.project.calendarobjects.CalendarObject;
import com.group2.project.calendarobjects.CalendarType;
import com.group2.project.calendarobjects.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
public class CalendarActivity
{

    // This class defines a high-level object representation for our CalendarObjects that will be used within our CalendarController.

    private CalendarType type;

    private ZonedDateTime date;
    //private Integer serviceNo;
    private CalendarObject calendarObject;

//    public CalendarActivity(ZonedDateTime date, String title, Integer serviceNo) { // old standard
//        this.date = date;
//        this.title = title;
//        this.serviceNo = serviceNo;
//    }

    public CalendarActivity(CalendarType ty, ZonedDateTime date, CalendarObject obj)
    {
        type = ty;
        this.date = date;
        calendarObject = obj;
    }

    // unused method that provides programmatic insight as to how to make the compiler allow you to downcast, which is important to us for writing to a json!
    public CalendarType getNativeType()
    {
        if(calendarObject instanceof Meeting)
        {
            return CalendarType.MEETING;
        }
        else if(calendarObject instanceof Event)
        {
            return CalendarType.EVENT;
        }
        else
        {
            return CalendarType.CALENDAR_OBJECT;
        }

    }

    @Override
    public String toString() {
        return(type + " : {" +
                calendarObject.toString() +
                "}"
        );
    }

}
