package com.group2.project.addressbook;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Marina Ljuboja
 *
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Contacts {

    //had id here but got an error and program wouldn't build
    @Id
    private long id;

    @Embedded
    private Person person;

}
