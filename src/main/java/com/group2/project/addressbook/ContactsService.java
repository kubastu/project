package com.group2.project.addressbook;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

/**
 * Author: Marina Ljuboja
 *
 *
 */
@Service
@Log4j2
public class ContactsService {
    @Autowired
    private ContactsRepository repo;

    public List<Person> list() {
        log.traceEntry("Entered a list");
        var retval = repo.findAll();
        log.traceExit("Exited from list", retval);
        return repo.findAll();
    }

    public Person save(Person person) {
        log.traceEntry("Enter saved", person);
        repo.save(person);
        log.traceExit("Exit saved", person);
        return person;
    }

    public void delete(long id) {
        log.traceEntry("Enter deleted", id);
        repo.deleteById(id);
        log.traceExit("Exit deleted");
    }
}
