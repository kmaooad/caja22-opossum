package edu.kmaooad;

import edu.kmaooad.model.Activity;
import edu.kmaooad.service.ActivityService;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
//public class DBSampleTest {
//
//    @Autowired
//    ActivityService activityService;
//    @Autowired
//    GroupService groupService;
//    @Test
//    public void test() throws ServiceException {
//        Activity a = new Activity();
//        List<Activity> act = new ArrayList<>();
//
//        a.setId("6393789438a910210ff8ced0");
//        a.setName("name");
//        act.add(a);
//        activityService.updateActivities(act);
//    }
//}