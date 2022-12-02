package edu.kmaooad;


import edu.kmaooad.model.Group;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.GroupRepository;
import edu.kmaooad.repositories.StudentRepository;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    final static List<Student> studentsList = new ArrayList<>();
    final static String missingID = "4";

    final static Optional<Student> missingStudent = Optional.empty();
    final static String group1ID = "1";
    final static Group group1 = new Group();

    final static String activityId = "1";
    final static String activityInGroupId = "2";

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        student1.setId(sID1);
        student2.setId(sID2);
        student3.setId(sID3);

        student1.setEmail(sEmail1);
        student2.setEmail(sEmail2);
        student3.setEmail(sEmail3);

        studentsList.add(student1);
        studentsList.add(student2);
        studentsList.add(student3);

        group1.setId(group1ID);
        group1.getActivities().add(activityInGroupId);
        student1.setGroupId(group1ID);
        student2.setGroupId(group1ID);
        student3.setGroupId(group1ID);


        Mockito.doReturn(student3).when(studentRepository).findByEmail(sEmail3);
        Mockito.doReturn(Optional.of(student2)).when(studentRepository).findById(sID2);
        Mockito.doReturn(Optional.of(student1)).when(studentRepository).findById(sID1);
        Mockito.doReturn(missingStudent).when(studentRepository).findById(missingID);

        Mockito.doReturn(Optional.of(group1)).when(groupRepository).findById(group1ID);

    }

    @Test
    public void addStudents() {
        List<Student> notAdded = studentService.addStudents(studentsList);
        List<Student> assumeInDB = new ArrayList<>();
        assumeInDB.add(student3);
        for (Student s : notAdded) {
            assertTrue(assumeInDB.contains(s));
        }
    }

    @Test
    public void updateStudents() {
        List<Student> notAdded = studentService.updateStudents(studentsList);
        List<Student> assumeNotInDB = new ArrayList<>();
        assumeNotInDB.add(student3);
        for (Student s : notAdded) {
            assertTrue(assumeNotInDB.contains(s));
        }
    }


    @Test
    public void addAndDeleteActivityStudent() {
        assertTrue(studentService.addStudentActivity(sID1, activityId));
        assertFalse(studentService.addStudentActivity(missingID, activityId));
        assertFalse(studentService.addStudentActivity(sID1, activityInGroupId));
        assertTrue(studentService.deleteStudentActivity(sID1, activityId));
        assertFalse(studentService.deleteStudentActivity(missingID, activityId));
    }


}
