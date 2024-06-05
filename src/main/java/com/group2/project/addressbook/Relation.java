package com.group2.project.addressbook;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document
public class Relation {
    enum RelationshipType {
        BUSINESS,
        FAMILY,
        FRIEND
    }
    @Id
    private String id;
    private RelationshipType relationshipType;

}
