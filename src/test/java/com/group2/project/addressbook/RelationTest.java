package com.group2.project.addressbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@DataMongoTest
public class RelationTest {
    @Autowired
    private RelationRepository relationRepo;

    @BeforeEach
    void setupDb() {
        relationRepo.deleteAll();
    }

    @DisplayName("Test adding relations")
    @Test
    public void testAddingRelation() {
        var relation = new Relation();
        relation.setRelationshipType("Business");

        var beforeAdd = relationRepo.count();
        relationRepo.save(relation);
        var afterAdd = relationRepo.count();

        assertEquals(beforeAdd + 1, afterAdd);

        var relationList = relationRepo.findByRelationshipType("Business");
        assertEquals(1, relationList.size());


    }

    @DisplayName("Test deleting relations")
    @Test
    public void testDeletingRelation() {
        var relation = new Relation();
        relation.setRelationshipType("Business");

        var beforeDelete = relationRepo.count();
        relationRepo.save(relation);
        relationRepo.delete(relation);
        var afterDelete = relationRepo.count();

        assertEquals(beforeDelete, afterDelete);

        var relationList = relationRepo.findByRelationshipType("Business");
        assertEquals(0, relationList.size());
    }
}


