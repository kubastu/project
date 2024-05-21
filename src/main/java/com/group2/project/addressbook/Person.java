package com.group2.project.addressbook;


/**
 * Author: Marina Ljuboja
 * Main idea: Separate class for specific info on each person in addressbook
 *
 */

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.Embeddable;

@Data
@Embeddable
public class Person {

    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
}
