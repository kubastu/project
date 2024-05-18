package com.group2.project.addressbook;

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
public class Contacts {
    private Person person;

}
