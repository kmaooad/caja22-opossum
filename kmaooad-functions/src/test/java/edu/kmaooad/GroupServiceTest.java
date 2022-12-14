package edu.kmaooad;


import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.repositories.GroupRepository;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.ServiceException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GroupServiceTest {


    @InjectMocks
    GroupService groupService;

    @Mock
    GroupRepository groupRepository;

    final Group group1 = new Group();
    final Group group2 = new Group();
    final String group1Name = "group1";
    final String group2Name = "group2";
    final String group1ID = "1";
    final String group2ID = "2";
    final String missingID = "3";
    final Optional<Group> missingGroup = Optional.empty();
    final String student1ID = "1";
    final Activity activity = new Activity();

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        group1.setId(group1ID);
        group1.setName("group1");
        group1.setYear(2022);
        group1.setGrade(1);
        group2.setId(group2ID);
        group2.setName("group2");
        group2.setYear(2023);
        group2.setGrade(2);

        activity.setId("1");
        Mockito.doReturn(Optional.of(group1)).when(groupRepository).findById(group1ID);
        Mockito.doReturn(List.of(group1, group2)).when(groupRepository).findAll();
        Mockito.doReturn(Optional.of(group1)).when(groupRepository).findByName(group1Name);
        Mockito.doReturn(missingGroup).when(groupRepository).findById(missingID);
    }

    @Test
    public void getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        assertEquals(groups.size(), 2 );
        assertTrue(groups.contains(group1));
        assertTrue(groups.contains(group2));
    }


    @Test
    public void addGroup() throws ServiceException {
        assertEquals(groupService.addGroup(group2), group2);
        Mockito.doReturn(Optional.of(group2)).when(groupRepository).findByName(group2Name);
        assertThrows(ServiceException.class, () -> groupService.addGroup(group2));
    }

    @Test
    public void updateGroup() throws ServiceException {
        group1.setName("group2");
        group1.setYear(2023);
        group1.setGrade(2);
        assertEquals(groupService.updateGroup(group1), group1);
        Group group2 = new Group();
        group2.setId(missingID);
        assertThrows(ServiceException.class, () -> groupService.updateGroup(group2));
    }

    @Test
    public void getGroupByName() {
        assertEquals(group1, groupService.getGroupByName(group1Name));
        assertNull(groupService.getGroupById("test"));
    }

    @Test
    public void getGroupById() {
        assertEquals(group1, groupService.getGroupById(group1ID));
        assertNull(groupService.getGroupById("test"));
    }

    @Test
    public void deleteGroup() throws ServiceException {
        assertEquals(group1ID, groupService.deleteGroup(group1ID).getId());
        assertThrows(ServiceException.class, () -> groupService.deleteGroup(missingID));
    }

    @Test
    public void addAndDeleteStudentGroup() throws ServiceException {
        assertEquals(groupService.addStudentGroup(student1ID, group1ID), student1ID);
        assertThrows(ServiceException.class, () -> groupService.addStudentGroup(student1ID, group1ID));
        assertEquals(groupService.deleteStudentGroup(student1ID, group1ID), student1ID);
        assertThrows(ServiceException.class, () -> groupService.deleteStudentGroup(student1ID, group1ID));
    }

    @Test
    public void addAndDeleteActivityGroup() throws ServiceException {
        assertEquals(groupService.addActivityGroup(activity.getId(), group1ID), activity.getId());
        assertThrows(ServiceException.class, () -> groupService.addActivityGroup(activity.getId(), group1ID));
        assertEquals(groupService.deleteActivityGroup(activity.getId(), group1ID), activity.getId());
        assertThrows(ServiceException.class, () -> groupService.deleteActivityGroup(activity.getId(), group1ID));
    }


}
