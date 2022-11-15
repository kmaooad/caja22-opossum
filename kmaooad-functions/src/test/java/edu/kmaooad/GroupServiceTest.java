package edu.kmaooad;


import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.StudentRepository;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.StudentService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@FunctionalSpringBootTest
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class GroupServiceTest {


    @Autowired
    GroupService groupService;


    @Test
    public void addGroup() throws Exception {
        Group group = new Group();
        group.setName("group1");

        System.out.println( groupService.addGroup(group));


    }

    @Test
    public void updateGroup() throws Exception {
        Group group = new Group();
        group.setId("63728dc9487f3260fb323919");
        group.setName("group2");

        System.out.println( groupService.updateGroup(group));


    }

    @Test
    public void deleteGroup() throws Exception {
        System.out.println( groupService.deleteGroup("63728a15cb1e30197bac4b3a"));


    }


    @Test
    public void addStudentGroup() throws Exception {

      //  System.out.println(groupService.addStudentGroup( "123", "63728dc9487f3260fb323919"));

        // System.out.println(groupService.deleteStudentGroup( "123", "63728dc9487f3260fb323919"));

      //  System.out.println(groupService.addActivityGroup( "123", "63728dc9487f3260fb323919"));

        System.out.println(groupService.deleteActivityGroup( "123", "63728dc9487f3260fb323919"));


    }

}
