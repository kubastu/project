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
        person.setFirstName("Pat");
        person.setPersonLastName("Sajak");
        Contacts contacts = Contacts.builder().person(person).build();

        String expectedError = "Person=(firstName=Pat, lastName=Sajak)";
        String expectedNoError = "Passenger(id=0, person=Person(firstName=Pat, lastName=Sajak))";
        assertEquals(expectedNoError, contacts.toString());

    }


    @Autowired
    private ContactsRepository contactsRepo;

    @DisplayName("Test adding flights")
    @Test
    public void testAddingPassengerAsComposition() {
        var person = new Person();
        person.setFirstName("Pat");
        person.setPersonLastName("Sajak");
        var passenger = Contacts.builder().person(person).build();

        var b4Add = contactsRepo.count();
        contactsRepo.save(passenger);
        var afterAdd = contactsRepo.count();

        assertEquals(b4Add + 1, afterAdd);

        var passengerList = contactsRepo.findByPersonLastName("Sajak");
        assertEquals(1, passengerList.size());
    }


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}