package edu.kmaooad;

import edu.kmaooad.model.Student;
import edu.kmaooad.service.ActivityService;
import edu.kmaooad.service.ServiceException;
import edu.kmaooad.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DBSampleTest {

    @Autowired
    ActivityService activityService;
    @Autowired
    StudentService studentService;
    @Test
    public void test() throws ServiceException {


    }
}
