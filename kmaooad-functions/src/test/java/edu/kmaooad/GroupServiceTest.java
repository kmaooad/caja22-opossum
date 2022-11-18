package edu.kmaooad;


import edu.kmaooad.model.Group;
import edu.kmaooad.repositories.GroupRepository;
import edu.kmaooad.service.GroupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@FunctionalSpringBootTest
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class GroupServiceTest {


    @Autowired
    GroupService groupService;

    @MockBean
    GroupRepository groupRepository;

    final static Group group1 = new Group();
    final static String group1Name = "group1";
    final static  String group1ID = "1";
    final static String missingID = "2";
    final static Optional<Group> missingGroup = Optional.empty();
    final static  String student1ID = "1";
    final static  String activity1ID = "1";

@Before
public void initTest(){
    group1.setId(group1ID);
    group1.setName("group1");
    Mockito.doReturn(Optional.of(group1)).when(groupRepository).findById(group1ID);
    Mockito.doReturn(missingGroup).when(groupRepository).findById(missingID);
}
    @Test
    public void addGroup() {
        System.out.println( groupService.addGroup(group1));
        assertTrue(groupService.addGroup(group1));
        Mockito.doReturn(group1).when(groupRepository).findByName(group1Name);
        assertFalse(groupService.addGroup(group1));
    }

    @Test
    public void updateGroup() {
        group1.setName("group2");
        assertTrue(groupService.updateGroup(group1));
        Group group2 = new Group();
        group2.setId(missingID);
        assertFalse(groupService.updateGroup(group2));
    }

    @Test
    public void deleteGroup()  {
        assertTrue( groupService.deleteGroup(group1ID));
        assertFalse( groupService.deleteGroup(missingID));
    }

    @Test
    public void addAndDeleteStudentGroup(){
        assertTrue(groupService.addStudentGroup(student1ID, group1ID));
        assertFalse(groupService.addStudentGroup(student1ID, group1ID));
        assertTrue(groupService.deleteStudentGroup(student1ID, group1ID));
        assertFalse(groupService.deleteStudentGroup(student1ID, group1ID));
    }
    @Test
    public void addAndDeleteActivityGroup(){
        assertTrue(groupService.addActivityGroup(activity1ID, group1ID));
        assertFalse(groupService.addActivityGroup(activity1ID, group1ID));
        assertTrue(groupService.deleteActivityGroup(activity1ID, group1ID));
        assertFalse(groupService.deleteActivityGroup(activity1ID, group1ID));
    }




}
