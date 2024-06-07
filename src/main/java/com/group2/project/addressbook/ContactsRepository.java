package com.group2.project.addressbook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;

/**
 * Author: Marina Ljuboja
 * Main idea: Repository using jpa repository as discussed in class
 *
 */
public interface ContactsRepository extends JpaRepository<Contacts, Long>{
    //private static HashMap<Long, Contacts> CONTACTS = new HashMap<Long, Contacts>();
    //List<Contacts> findByPersonLastName(String lastName);

    //public long countById(Long Id);

    //List<Contacts> findByPersonFirstName(String firstName);

}
