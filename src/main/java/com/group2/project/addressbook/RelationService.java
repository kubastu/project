package com.group2.project.addressbook;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RelationService {
    @Autowired
    private RelationRepository repo;

    public List<Relation> list() {
        log.traceEntry("Enter list");
        var retval = repo.findAll();
        log.traceExit("Exit list", retval);
        return repo.findAll();
    }

    public String save(Relation relation) {
        log.traceEntry("enter save", relation);
        repo.save(relation);
        log.traceExit("exit save", relation);
        return relation.getId();
    }

    public void delete(String id) {
        log.traceEntry("Enter delete", id);
        repo.deleteById(id);
        log.traceExit("Exit delete");
    }
}
