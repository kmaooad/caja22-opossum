package edu.kmaooad;


import edu.kmaooad.model.Group;
import edu.kmaooad.repositories.GroupRepository;
import edu.kmaooad.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GroupServiceTest {


    @InjectMocks
    GroupService groupService;

    @Mock
    GroupRepository groupRepository;

    final Group group1 = new Group();
    final String group1Name = "group1";
    final String group1ID = "1";
    final String missingID = "2";
    final Optional<Group> missingGroup = Optional.empty();
    final String student1ID = "1";
    final String activity1ID = "1";

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        group1.setId(group1ID);
        group1.setName("group1");
        group1.setYear(2022);
        group1.setGrade(1);
        Mockito.doReturn(Optional.of(group1)).when(groupRepository).findById(group1ID);
        Mockito.doReturn(missingGroup).when(groupRepository).findById(missingID);
    }

    @Test
    public void addGroup() {
        System.out.println(groupService.addGroup(group1));
        assertTrue(groupService.addGroup(group1));
        Mockito.doReturn(group1).when(groupRepository).findByName(group1Name);
        assertFalse(groupService.addGroup(group1));
    }

    @Test
    public void updateGroup() {
        group1.setName("group2");
        group1.setYear(2023);
        group1.setGrade(2);
        assertTrue(groupService.updateGroup(group1));
        Group group2 = new Group();
        group2.setId(missingID);
        assertFalse(groupService.updateGroup(group2));
    }

    @Test
    public void deleteGroup() {
        assertTrue(groupService.deleteGroup(group1ID));
        assertFalse(groupService.deleteGroup(missingID));
    }

    @Test
    public void addAndDeleteStudentGroup() {
        assertTrue(groupService.addStudentGroup(student1ID, group1ID));
        assertFalse(groupService.addStudentGroup(student1ID, group1ID));
        assertTrue(groupService.deleteStudentGroup(student1ID, group1ID));
        assertFalse(groupService.deleteStudentGroup(student1ID, group1ID));
    }

    @Test
    public void addAndDeleteActivityGroup() {
        assertTrue(groupService.addActivityGroup(activity1ID, group1ID));
        assertFalse(groupService.addActivityGroup(activity1ID, group1ID));
        assertTrue(groupService.deleteActivityGroup(activity1ID, group1ID));
        assertFalse(groupService.deleteActivityGroup(activity1ID, group1ID));
    }


}
