package com.group2.project.addressbook;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RelationRepository extends MongoRepository<Relation, String>{
    //List<Relation.RelationshipType> findByRelationshipType(Relation.relationshipType);
}
