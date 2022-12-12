package edu.kmaooad;


import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.ActivityRepository;
import edu.kmaooad.service.ActivityService;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ActivityServiceTest {


    @InjectMocks
    ActivityService activityService;

    @Mock
    ActivityRepository activityRepository;

    final Activity activity1 = new Activity();
    final Activity activity2 = new Activity();
    final String activityName1 = "activity1";
    final String activityName2 = "activity2";
    final String activity1ID = "1";
    final String activity2ID = "2";
    final String missingID = "3";
    final Optional<Activity> missingActivity = Optional.empty();
    final Activity activity = new Activity();

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        activity1.setId(activity1ID);
        activity1.setName(activityName1);
        activity1.setStatus("");
        activity1.setStartDate(new Date());
        activity1.setEndDate(new Date());

        activity2.setId(activity2ID);
        activity2.setName(activityName2);
        activity2.setStatus("");
        activity2.setStartDate(new Date());
        activity2.setEndDate(new Date());

        Mockito.doReturn(Optional.of(activity1)).when(activityRepository).findById(activity1ID);
        Mockito.doReturn(Optional.of(activity1)).when(activityRepository).findByName(activityName1);

        Mockito.doReturn(Optional.of(activity1)).when(activityRepository).findById(activity2ID);
        Mockito.doReturn(Optional.of(activity1)).when(activityRepository).findByName(activityName2);
        Mockito.doReturn(List.of(activity1, activity2)).when(activityRepository).findAll();
        Mockito.doReturn(missingActivity).when(activityRepository).findById(missingID);
    }

    @Test
    public void addActivity() throws ServiceException {
        activity.setName("activity7");
        activity.setStatus("new st");
        activity.setStartDate(new Date());
        activity.setEndDate(new Date());
        assertEquals(activityService.addActivity(activity), activity);
        assertThrows(ServiceException.class, () -> activityService.addActivity(activity1));
    }

    @Test
    public void updateActivity() throws ServiceException {
        activity1.setName("activity2Upd");
        activity1.setStatus("new st");
        activity1.setStartDate(new Date());
        activity1.setEndDate(new Date());
        assertEquals(activityService.updateActivities(List.of(activity1)).get(0), activity1);
        Activity activity2 = new Activity();
        activity2.setId(missingID);
        assertThrows(ServiceException.class, () -> activityService.updateActivities(List.of(activity2)));
    }

    @Test
    public void getActivityByName() {
        assertEquals(activityService.getActivityByName(activityName1), activity1);
        assertNull(activityService.getActivityByName("name doesnt exist"));
    }

    @Test
    public void getStatusOfActivitiesForGroupTest() {
        Group gr = new Group();
        gr.setActivities(List.of(activity1ID));
        List<String> result = activityService.getStatusOfActivitiesForGroup(gr);
        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertTrue(result.contains(activityName1 + ' ' + GlobalConstants.ASSIGNED));
        assertTrue(result.contains(activityName2));
    }

    @Test
    public void getStatusOfActivitiesForStudentTest() {
        Student st = new Student();
        st.setActivities(List.of(activity1ID));
        List<String> result = activityService.getStatusOfActivitiesForStudent(st);
        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertTrue(result.contains(activityName1 + ' ' + GlobalConstants.ASSIGNED));
        assertTrue(result.contains(activityName2));
    }


}
