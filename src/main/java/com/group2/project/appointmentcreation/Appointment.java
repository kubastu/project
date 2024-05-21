package com.group2.project.appointmentcreation;

import com.group2.project.calendarobjects.CalendarObject;

import java.util.Date;

public class Appointment extends CalendarObject {
  private String location;
  private Date time;

  public Appointment(Date date, String title, String description, String location){
    super(date, title, description);
    this.location= location;
  }
}

