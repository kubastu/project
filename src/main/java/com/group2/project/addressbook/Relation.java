package com.group2.project.addressbook;

import jakarta.persistence.OneToOne;
import lombok.*;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Id;

@Data
//@ToString
//@EqualsAndHashCode
//@Getter
//@RequiredArgsConstructor
@Document("relation")
public class Relation {

    @Id
    private String id;
    private String relationshipType;

    @OneToOne
    @Reference
    private Contacts contacts;

    @Reference Person person;


}
