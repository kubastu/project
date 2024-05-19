package com.group2.project.addressbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

/**
 * Author: Marina Ljuboja
 * Main idea: Controller using swagger and java api
 *
 */

@RestController
@RequestMapping("/api/addressbook")
@Tag(name = "Contacts", description = "Everything about your contact")
@Log4j2

public class ContactsController {
    @Autowired
    private ContactsService service;

    @GetMapping
    @Operation(summary = "Adds info to addressbook to database")
    @ApiResponse(responseCode = "200", description = "valid response",
            content = {@Content(mediaType="application/json", schema=@Schema(implementation=Person.class))})
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

    @PostMapping("/validated")
    @Operation(summary = "Saves contact info and returns person id")
    public ResponseEntity<String> validatedSave(@Valid @RequestBody Person person) {
        log.traceEntry("Enter saved", person);
        service.save(person);
        log.traceExit("Exit saved", person);
        return ResponseEntity.ok("The new id is " + person.getId());
    }

    @DeleteMapping
    @Operation(summary = "Delete the person")
    public void delete(long id) {
        log.traceEntry("Enter delete", id);
        service.delete(id);
        log.traceExit("Exit delete");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
