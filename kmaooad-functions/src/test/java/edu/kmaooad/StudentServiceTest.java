package edu.kmaooad;


import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.GroupRepository;
import edu.kmaooad.repositories.StudentRepository;
import edu.kmaooad.service.ServiceException;
import edu.kmaooad.service.StudentService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StudentServiceTest {


    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    GroupRepository groupRepository;

    final static Student student1 = new Student();
    final static Student student2 = new Student();
    final static Student student3 = new Student();
    final static String sID1 = "1";
    final static String sID2 = "2";
    final static String sID3 = "3";
    final static String sEmail1 = "e1";
    final static String sEmail2 = "e2";
    final static String sEmail3 = "e3";
    static List<Student> studentsList = new ArrayList<>();
    final static String missingID = "4";

    final static Optional<Student> missingStudent = Optional.empty();
    final static String group1ID = "1";
    final static Group group1 = new Group();

    final static String activityId = "1";
    final static String activityInGroupId = "2";
    final Activity activity = new Activity();
    final Activity activityInDb = new Activity();
    final Activity activityInGroup = new Activity();

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        student1.setId(sID1);
        student2.setId(sID2);
        student3.setId(sID3);

        student1.setEmail(sEmail1);
        student2.setEmail(sEmail2);
        student3.setEmail(sEmail3);

        studentsList = new ArrayList<>();
        studentsList.add(student1);
        studentsList.add(student2);


        group1.setId(group1ID);
        activityInGroup.setId(activityInGroupId);
        activity.setId(activityId);
        activityInDb.setId(activityInGroupId);
        group1.getActivities().add(activityInDb.getId());
        student1.setGroupId(group1ID);
        student2.setGroupId(group1ID);
        student3.setGroupId(group1ID);


        Mockito.doReturn(Optional.of(student3)).when(studentRepository).findByEmail(sEmail3);
        Mockito.doReturn(Optional.of(student2)).when(studentRepository).findById(sID2);
        Mockito.doReturn(Optional.of(student1)).when(studentRepository).findById(sID1);
        Mockito.doReturn(missingStudent).when(studentRepository).findById(missingID);

        Mockito.doReturn(Optional.of(group1)).when(groupRepository).findById(group1ID);

    }

    @Test
    public void addStudents() throws ServiceException {
        List<Student> addedAdded = studentService.addStudents(studentsList);
        List<Student> assumeInDB = new ArrayList<>();
        assumeInDB.add(student1);
        assumeInDB.add(student2);
        for (Student s : addedAdded) {
            assertTrue(assumeInDB.contains(s));
        }
        studentsList.add(student3);
        assertThrows(ServiceException.class, () -> studentService.addStudents(studentsList));
    }

    @Test
    public void getStudentByEmail() {
        assertEquals(student3, studentService.getStudentByEmail(sEmail3));
        assertNull(studentService.getStudentByEmail("test"));
    }

    @Test
    public void updateStudents() throws ServiceException {
        List<Student> notAdded = studentService.updateStudents(studentsList);
        List<Student> assumeInDB = new ArrayList<>();
        assumeInDB.add(student1);
        assumeInDB.add(student2);
        for (Student s : notAdded) {
            assertTrue(assumeInDB.contains(s));
        }
        studentsList.add(student3);
        assertThrows(ServiceException.class, () -> studentService.updateStudents(studentsList));
    }


    @Test
    public void addAndDeleteActivityStudent() throws ServiceException {
        assertEquals(studentService.addStudentActivity(activity.getId(), sID1), activity.getId());
        assertThrows(ServiceException.class, () -> studentService.addStudentActivity(activityInDb.getId(), missingID));
        assertThrows(ServiceException.class, () -> studentService.addStudentActivity(activityInGroup.getId(), sID1));
        assertEquals(studentService.deleteStudentActivity(activity.getId(), sID1), activity.getId());
        assertThrows(ServiceException.class, () -> studentService.deleteStudentActivity(activity.getId(), missingID));
    }


}
