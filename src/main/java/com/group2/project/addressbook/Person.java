package com.group2.project.addressbook;


/**
 * Author: Marina Ljuboja
 * Main idea: Separate class for specific info on each person in addressbook
 *
 */


import lombok.Data;
import jakarta.persistence.Embeddable;


@Data
@Embeddable

public class Person {

    private String firstName;
    private String personLastName;
    private String address;
}
