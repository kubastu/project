package com.group2.project.addressbook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContactsServiceTest {

    @Autowired
    private ContactsRepository repo;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}

