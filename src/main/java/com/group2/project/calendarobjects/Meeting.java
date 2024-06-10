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
public class Meeting extends Event
{

    // todo : Add addressbook implementation
    // todo:    --> proposed: ArrayList<Contacts> AddressBook

    @Column(name = "LOCATION")
    private String location;

    public Meeting()
    {
        super();
    }

    public Meeting(ZonedDateTime date, String title, String description, String time, String location) {
        super(date, title, description, time);
        this.location = location;
    }

    // old standard:
//    @Override
//    public String toString() {
//        return "Meeting{" +
//                "title=" + getTitle() +
//                "description=" + getDescription() +
//                "time=" + getTime() +
//                "location='" + location + '\'' +
//                '}';
//    }

    // much cleaner for displaying in calendar:
    @Override
    public String toString() {
        return('\'' + getTitle() + "', '" + getDescription() + "', " + getTime() + " @ '" + getLocation() + '\'');
    }
}
