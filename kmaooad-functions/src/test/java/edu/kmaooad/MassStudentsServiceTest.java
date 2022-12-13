package edu.kmaooad;

import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.StudentRepository;
import edu.kmaooad.service.ActivityService;
import edu.kmaooad.service.MassStudentsService;
import edu.kmaooad.service.ServiceCSVException;
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
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MassStudentsServiceTest {
    @InjectMocks
    MassStudentsService massStudentsService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    ActivityService activityService;

    @Mock
    JavaMailSender mailSender;

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        Activity activity1 = new Activity("aid1", "name1", LocalDate.now(), LocalDate.now(), "st");
        Activity activity2 = new Activity("aid2", "name2", LocalDate.now(), LocalDate.now(), "st");
        Activity activity3 = new Activity("aid3", "name3", LocalDate.now(), LocalDate.now(), "st");
        Mockito.doReturn(activity1).when(activityService).getActivityById("aid1");
        Mockito.doReturn(activity2).when(activityService).getActivityById("aid2");
        Mockito.doReturn(activity3).when(activityService).getActivityById("aid3");
        Mockito.doReturn(true).when(activityService).existById(anyString());
    }

    @Test
    public void generateStudentCSVTest() {
        Student student1 = new Student("id1", "fn1", "ln1", "p1", "d1", "em1", new ArrayList<>());
        Student student2 = new Student("id2", "fn2", "ln2", "p2", "d2", "em2", List.of("aid1", "aid2"));
        String csv = massStudentsService.generateStudentCSV(List.of(student1, student2));
        assertNotNull(csv);
        String[] lines = csv.split("\n");
        assertEquals(lines.length, 2);
        assertEquals(lines[0].split(",").length, 5);
        assertEquals(lines[1].split(",").length, 7);
    }

    @Test
    public void parseStudentCSVandReplaceAllTest() throws ServiceCSVException {
        String st1 = "ln1,fn1,p1,em1,d1\n";
        String st2 = "ln2,fn2,p2,em2,d2,aid1,aid3\n";
        String st3 = "ln3,fn3,p3,em3,d1,aid1,aid1,aid2";
        String lines = st1 + st2 + st3;
        List<Student> res = massStudentsService.parseStudentCSV(lines);
        assertEquals(res.size(), 3);
    }

    @Test
    public void failParamsStudentCSVandReplaceAllTest() {
        try {
            String st1 = "ln1,fn1,em1,d1\n";
            String st2 = "ln2,fn2,p2,em2,d2,aid1,aid3\n";
            String st3 = "ln3,fn3,p3,em3,d1,aid1,aid1,aid2";
            String lines = st1 + st2 + st3;
            massStudentsService.parseStudentCSV(lines);
            fail();
        } catch (ServiceCSVException e) {
            assertEquals(e.getErrorType(), ServiceCSVException.TypeOfCSVException.NOT_ENOUGH_PARAMS_ON_LINE);
        }
    }

    @Test
    public void failDubEmailStudentCSVandReplaceAllTest() {
        try {
            String st1 = "ln1,fn1,p1,em1,d1\n";
            String st2 = "ln2,fn2,p2,em2,d2,aid1,aid3\n";
            String st3 = "ln3,fn3,p3,em2,d1,aid1,aid1,aid2";
            String lines = st1 + st2 + st3;
            massStudentsService.parseStudentCSV(lines);
            fail();
        } catch (ServiceCSVException e) {
            assertEquals(e.getErrorType(), ServiceCSVException.TypeOfCSVException.EMAIL_DUPLICATE);
        }
    }


}
