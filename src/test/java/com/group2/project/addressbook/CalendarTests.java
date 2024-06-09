package com.group2.project.addressbook;


import com.group2.project.calendarobjects.CalendarObject;
import com.group2.project.calendarobjects.Event;
import com.group2.project.calendarobjects.Meeting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class CalendarTests
{

    @DisplayName("CalendarObjects Lombok Test")
    @Test
    public void testCalendarObjects()
    {
        CalendarObject testObj = new CalendarObject();
        testObj.setDate(ZonedDateTime.now());
        testObj.setTitle("Title");
        testObj.setDescription("Description");
        assertEquals(testObj.getTitle(), "Title");
        assertEquals(testObj.getDescription(), "Description");
    }

    @DisplayName("Event Lombok Test")
    @Test
    public void testEvent()
    {
        Event testEvent = new Event();
        testEvent.setDate(ZonedDateTime.now());
        testEvent.setTitle("EventTitle");
        testEvent.setDescription("EventDescription");
        testEvent.setTime("13:00");
        assertEquals(testEvent.getTitle(), "EventTitle");
        assertEquals(testEvent.getDescription(), "EventDescription");
        assertEquals(testEvent.getTime(), "13:00");
    }

    @DisplayName("Meeting Lombok Test")
    @Test
    public void testMeeting()
    {
        Meeting testMeeting = new Meeting();
        testMeeting.setDate(ZonedDateTime.now());
        testMeeting.setTitle("EventTitle");
        testMeeting.setDescription("EventDescription");
        testMeeting.setTime("13:00");
        testMeeting.setLocation("Chicago");
        assertEquals(testMeeting.getTitle(), "EventTitle");
        assertEquals(testMeeting.getDescription(), "EventDescription");
        assertEquals(testMeeting.getTime(), "13:00");
        assertEquals(testMeeting.getLocation(), "Chicago");
    }


}
