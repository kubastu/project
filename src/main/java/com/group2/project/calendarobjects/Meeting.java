package com.group2.project.calendarobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Entity
@Getter
public class Meeting extends CalendarObject
{

    // todo : Add addressbook implementation
    // todo:    --> proposed: ArrayList<Contacts> AddressBook

    @Column(name = "LOCATION")
    private String location;

    public Meeting(Date date, String title, String description, String location) {
        super(date, title, description);
        this.location = location;
    }
}
