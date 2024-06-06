package com.group2.project.addressbook;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/relation")
@Tag(name = "Preference", description = "Everything about your Passenger Preference")
@Log4j2
public class RelationController {
    @Autowired
    private RelationService service;

    @GetMapping
    @Operation(summary = "Returns all the Passenger Preference in the database")
    @ApiResponse(responseCode = "200", description = "valid response",
            content = {@Content(mediaType="application/json", schema=@Schema(implementation=Relation.class))})
    public List<Relation> list() {
        return service.list();
    }

    @PostMapping
    @Operation(summary = "Save the Passenger and returns the Passenger id")
    public String save(Relation relation) {
        log.traceEntry("enter save", relation);
        service.save(relation);
        log.traceExit("exit save", relation);
        return relation.getId();
    }

    @DeleteMapping
    @Operation(summary = "Delete the Passenger Preference")
    public void delete(String id) {
        log.traceEntry("Enter delete", id);
        service.delete(id);
        log.traceExit("Exit delete");
    }

}
