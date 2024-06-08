package com.group2.project.addressbook;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

/**
 * Author: Marina Ljuboja
 * Description: I use lombok here.
 *
 */
@Service
@Log4j2
public class ContactsService {
    @Autowired
    private ContactsRepository repo;

    public List<Contacts> list() {
        log.traceEntry("Entered a list");
        var returnval = repo.findAll();
        log.traceExit("Exited from list", returnval);
        return repo.findAll();
    }

    public Contacts save(Contacts contacts) {
        log.traceEntry("Enter saved", contacts);
        repo.save(contacts);
        log.traceExit("Exit saved", contacts);
        return contacts;
    }

    public void delete(long id) {
        log.traceEntry("Enter deleted", id);
        repo.deleteById(id);
        log.traceExit("Exit deleted");
    }


}
