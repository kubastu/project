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
    enum RelationshipType {
        BUSINESS,
        FAMILY,
        FRIEND
    }
    @Id
    private String id;
    private RelationshipType relationshipType;

    @OneToOne
    @Reference
    private Contacts contacts;

    @Reference Person person;

    @NonNull
    private RelationshipType type;

}
