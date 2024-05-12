package com.group2.project.calendarobjects;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Entity
@Getter
public class Event extends CalendarObject
{

    @Column(name = "TIME")
    private String time;

    public Event(Date date, String title, String description, String time) {
        super(date, title, description);
        this.time = time;
    }
}
