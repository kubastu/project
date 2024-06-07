package com.group2.project.addressbook;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RelationRepository extends MongoRepository<Relation, String>{
    //public Relation findById(id);
}
