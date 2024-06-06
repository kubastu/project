//package com.group2.project.addressbook;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class RelationRepositoryTest {
//    @Autowired
//    private ContactsRepository contactsRepo;
//
//    @Test
//    public void testAddFind() {
//        String personLastName = "Doe";
//        //Person person = new Person();
//
//        contactsRepo.save("Jane Doe");
//
//        University depaul = new University("DePaul");
//        uniRepo.save(depaul);
//        ArrayList<University> unis = new ArrayList<>();
//        unis.add(depaul);
//
//        instructor.setUniversities(unis);
//        instructorRepo.save(instructor);
//
//        Instructor findInstructor = instructorRepo.findByName(name);
//        assertEquals(instructor.getId(), findInstructor.getId());
//        assertEquals(instructor.getUniversities().size(), findInstructor.getUniversities().size());
//    };
//
//
//    @Test
//    public void testAddFind() {
//        String name = "Hannah Yu";
//        Instructor instructor = new Instructor(name);
//
//        instructorRepo.save(instructor);
//
//        University depaul = new University("DePaul");
//        uniRepo.save(depaul);
//        ArrayList<University> unis = new ArrayList<>();
//        unis.add(depaul);
//
//        instructor.setUniversities(unis);
//        instructorRepo.save(instructor);
//
//        Instructor findInstructor = instructorRepo.findByName(name);
//        assertEquals(instructor.getId(), findInstructor.getId());
//        assertEquals(instructor.getUniversities().size(), findInstructor.getUniversities().size());
//    };
//}
//

