package com.group2.project.addressbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;

/**
 * Author: Marina Ljuboja
 * Main idea: Controller using swagger and java api
 * Description: This is where I have the CRUD operations
 */

@RestController
@RequestMapping("/api/addressbook")
@Log4j2

public class ContactsController {
    @Autowired
    private ContactsService service;

    @GetMapping
    @Operation(summary = "Adds info to addressbook to database")
    public List<Person> list() {
        return service.list();
    }

    @PostMapping
    @Operation(summary = "Saves contact info and returns person id")
    public long save(Person person) {
        log.traceEntry("Enter saved", person);
        service.save(person);
        log.traceExit("Exit saved", person);
        return person.getId();
    }


    @DeleteMapping
    @Operation(summary = "Delete the person")
    public void delete(long id) {
        log.traceEntry("Enter delete", id);
        service.delete(id);
        log.traceExit("Exit delete");
    }

}
