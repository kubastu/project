package com.group2.project.addressbook;


/**
 * Author: Marina Ljuboja
 * Main idea: Separate class for specific info on each person in addressbook
 *
 */

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Data
@Embeddable
//@Entity
//@Getter
public class Person {

    //@Id
    //@GeneratedValue
    //private long id;
    private String firstName;
    private String personLastName;
}
