package com.group2.project.addressbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonTest {
    @DisplayName("Lombok Test")
    @Test
    public void testLombok() {
        Person john = new Person();
        john.setFirstName("John");
        john.setPersonLastName("Smith");
        john.setAddress("1 E Jackson Blvd, Chicago, IL");
        String expectedWithError = "John Smith 1 E Jackson Blvd, Chicago, IL";
        String expectedNoError = "Person(firstName=John, personLastName=Smith, address=1 E Jackson Blvd, Chicago, IL)";
        assertEquals(expectedNoError, john.toString());
    }
}
