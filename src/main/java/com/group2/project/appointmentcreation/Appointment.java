package com.group2.project.appointmentcreation;

//import com.group2.project.calendarobjects.CalendarObject;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
import com.group2.project.calendarobjects.CalendarObject;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Appointment extends CalendarObject {

  @Id
  private long id;

  private String location;
  private Date time;

  public Appointment(ZonedDateTime date, String title, String description, String location){
    super(date, title, description);
    this.location= location;
  }
}

