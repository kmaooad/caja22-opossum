package edu.kmaooad;


import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.StudentRepository;
import edu.kmaooad.service.StudentService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@FunctionalSpringBootTest
@ExtendWith(SpringExtension.class)
public class StudentServiceTest {


    @Autowired
    StudentService studentService;
//
    @Autowired
StudentRepository rep;


    @Test
    public void addStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        Student s1 = new Student();
        s1.setFirstName("Vasia");
        s1.setEmail("s1");
        Student s2 = new Student();
        s2.setFirstName("Vasia");
        s2.setEmail("s2");
        students.add(s1);
        students.add(s2);
        System.out.println( studentService.addStudents(students));


    }

    @Test
    public void updateStudents() {
        List<Student> students = new ArrayList<>();
        Student s1 = new Student();
        s1.setFirstName("Mashka");
        s1.setId("6372787e65d24d72a2761a48");
        s1.setEmail("s1");
        s1.setLastName("123");
        s1.setGroupId("123");
        s1.setPatronym("123");
        s1.setDepartment("123");
        s1.setGroupId("123");

        students.add(s1);


        studentService.updateStudents(students);
    }
    @Test
    public void deleteActivityStudent() {

        System.out.println(studentService.deleteStudentActivity("6372787e65d24d72a2761a48", "1234"));
    }

}
