package com.group2.project.addressbook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ContactsTest {

    @DisplayName("Test composition in Lombok")
    @Test
    public void testLombok() {
        Person person = new Person();
        person.setFirstName("Jane");
        person.setPersonLastName("Doe");
        person.setAddress("1 E Jackson Blvd, Chicago, IL");
        Contacts contacts = Contacts.builder().person(person).build();

        String expectedError = "Person=(firstName=Jane, personLastName=Doe, address=null)";
        String expectedNoError = "Contacts(id=0, person=Person(firstName=Jane, personLastName=Doe, address=1 E Jackson Blvd, Chicago, IL))";
        assertEquals(expectedNoError, contacts.toString());
    }


    @Autowired
    private ContactsRepository contactsRepo;

    @DisplayName("Test adding contacts")
    @Test
    public void testAddingContacts() {
        var person = new Person();
        person.setFirstName("Jane");
        person.setPersonLastName("Doe");
        person.setAddress("1 E Jackson Blvd, Chicago, IL");
        var contacts = Contacts.builder().person(person).build();

        var beforeAdd = contactsRepo.count();
        contactsRepo.save(contacts);
        var afterAdd = contactsRepo.count();

        assertEquals(beforeAdd + 1, afterAdd);

        var contactsList = contactsRepo.findByPersonFirstName("Jane");
        assertEquals(1, contactsList.size());
    }

}