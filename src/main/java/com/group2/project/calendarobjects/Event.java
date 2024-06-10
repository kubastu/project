package com.group2.project.calendarobjects;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Entity
@Getter
public class Event extends CalendarObject
{

    @Column(name = "TIME")
    private String time;

    // JZ: "lombok requires base constructor in base class"
    public Event()
    {
        super();
    }

    public Event(ZonedDateTime date, String title, String description, String time) {
        super(date, title, description);
        this.time = time;
    }

    // old standard:
//    @Override
//    public String toString() {
//        return "Event{" +
//                "title=" + getTitle() +
//                "description=" + getDescription() +
//                "time='" + time + '\'' +
//                '}';
//    }

    // much cleaner for displaying in calendar:
    @Override
    public String toString() {
        return('\'' + getTitle() + "', '" + getDescription() + "', " + getTime());
    }
}
