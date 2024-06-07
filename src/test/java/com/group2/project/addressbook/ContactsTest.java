package com.group2.project.addressbook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

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
        Contacts contacts = Contacts.builder().person(person).build();

        String expectedError = "Person=(firstName=Jane, lastName=Doe)";
        String expectedNoError = "Passenger(id=0, person=Person(firstName=Jane, lastName=Doe))";
        assertEquals(expectedNoError, contacts.toString());

    }


    @Autowired
    private ContactsRepository contactsRepo;

    @DisplayName("Test adding flights")
    @Test
    public void testAddingPassengerAsComposition() {
        var person = new Person();
        person.setFirstName("Jane");
        person.setPersonLastName("Doe");
        var contacts = Contacts.builder().person(person).build();

        var beforeAdd = contactsRepo.count();
        contactsRepo.save(contacts);
        var afterAdd = contactsRepo.count();

        assertEquals(beforeAdd + 1, afterAdd);

        var passengerList = contactsRepo.findByPersonLastName("Doe");
        assertEquals(1, passengerList.size());
    }


}