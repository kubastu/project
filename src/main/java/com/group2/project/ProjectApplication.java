package com.group2.project;

import com.group2.project.CalendarObjects.CalendarObject;
import com.group2.project.CalendarObjects.Event;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
		CalendarObject test = new Event(new Date(), "my event", "this is my description", "11:00am");
		System.out.println(test.toString());
	}

}
