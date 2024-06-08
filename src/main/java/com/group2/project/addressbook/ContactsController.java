package com.group2.project.addressbook;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;

/**
 * Author: Marina Ljuboja
 * Main idea: Controller using swagger and java api
 * Description: This is where I have the CRUD operations and RESTful services
 */

@RestController
@RequestMapping("/api/addressbook")
@Tag(name = "Contacts", description = "Contacts Info")
@Log4j2

public class ContactsController {
    @Autowired
    private ContactsService service;

    @GetMapping
    @Operation(summary = "Adds info to addressbook to database")
    public List<Contacts> list() {
        return service.list();
    }

    @PostMapping
    @Operation(summary = "Saves contact info and returns person id")
    public long save(Contacts contacts) {
        log.traceEntry("Enter saved", contacts);
        service.save(contacts);
        log.traceExit("Exit saved", contacts);
        return contacts.getId();
    }


    @DeleteMapping
    @Operation(summary = "Delete the person")
    public void delete(long id) {
        log.traceEntry("Enter delete", id);
        service.delete(id);
        log.traceExit("Exit delete");
    }


}
