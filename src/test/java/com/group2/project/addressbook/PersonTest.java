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
        String expectedWithError = "John Smith";
        String expectedNoError = "Person(firstName=John, lastName=Smith)";
        assertEquals(expectedNoError, john.toString());
    }
}
