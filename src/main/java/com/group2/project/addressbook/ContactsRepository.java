package com.group2.project.addressbook;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Author: Marina Ljuboja
 * Main idea: Repository using jpa repository as discussed in class
 *
 */
public interface ContactsRepository extends JpaRepository<Person, Long>{
    List<Contacts> findByPersonLastName(String lastName);

    //List<Contacts> findByPersonFirstName(String firstName);

}
