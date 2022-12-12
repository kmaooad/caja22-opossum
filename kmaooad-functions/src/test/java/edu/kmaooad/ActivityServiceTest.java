package edu.kmaooad;


import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.kmaooad.constants.bot.GlobalConstants.ACTIVITY_STATUS_COMPLETED;
import static edu.kmaooad.constants.bot.GlobalConstants.ACTIVITY_STATUS_IN_PROGRESS;
import static edu.kmaooad.constants.bot.GroupConstants.ASSIGNED;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ActivityServiceTest {

    @InjectMocks
    ActivityService activityService;

    @Mock
    ActivityRepository activityRepository;

    final Group group1 = new Group();

    private  ArrayList<Activity> activities = new ArrayList();

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);

        group1.setId("1");
        group1.setName("group1");
        group1.setYear(2022);
        group1.setGrade(1);
        group1.setActivities(List.of("1"));

        Activity completed = new Activity();
        completed.setId("1");
        completed.setName("complete");
        completed.setStartDate(LocalDate.now(ZoneId.systemDefault()).minusDays(6));
        completed.setEndDate(LocalDate.now(ZoneId.systemDefault()).minusDays(2));

        Activity inProg = new Activity();
        inProg.setId("2");
        inProg.setName("prog");
        inProg.setStartDate(LocalDate.now(ZoneId.systemDefault()).minusDays(6));
        inProg.setEndDate(LocalDate.now(ZoneId.systemDefault()).plusDays(6));

        Activity haveStatus = new Activity();
        haveStatus.setId("3");
        haveStatus.setName("haveStat");
        haveStatus.setStartDate(LocalDate.now(ZoneId.systemDefault()).minusDays(6));
        haveStatus.setEndDate(LocalDate.now(ZoneId.systemDefault()).plusDays(6));
        haveStatus.setStatus(ACTIVITY_STATUS_IN_PROGRESS);

        Activity justActivity = new Activity();
        justActivity.setId("4");
        justActivity.setName("justActiv");
        justActivity.setStartDate(null);
        justActivity.setEndDate(null);

        activities.addAll(List.of(completed,inProg,haveStatus,justActivity));

        Mockito.doReturn(List.of(completed,inProg,haveStatus,justActivity)).when(activityRepository).findAll();
        Mockito.doReturn(Optional.of(completed)).when(activityRepository).findById("1");
        Mockito.doReturn(Optional.of(inProg)).when(activityRepository).findById("2");
        Mockito.doReturn(Optional.of(haveStatus)).when(activityRepository).findById("3");
        Mockito.doReturn(Optional.of(justActivity)).when(activityRepository).findById("4");

        Mockito.doReturn(Optional.of(completed)).when(activityRepository).findByName(completed.getName());
    }

    @Test
    public void testUpdatesStatuses() throws ServiceException {
        assertEquals(4,activityService.getAllActivities().size());
        assertEquals(activityService.getAllActivities().stream()
                .filter(s -> s.getStatus() != null && s.getStatus().equals(ACTIVITY_STATUS_IN_PROGRESS))
                .count(),1);
        assertEquals(activityService.getAllActivities().stream().filter(s -> s.getStatus() == null)
                .count(),3);

        activityService.updateStatuses();

        assertEquals(activityService.getAllActivities().size(),4);
        assertEquals(activityService.getAllActivities().stream()
                .filter(s -> s.getStatus() != null && s.getStatus().equals(ACTIVITY_STATUS_IN_PROGRESS))
                .count(),2);
        assertEquals(activityService.getAllActivities().stream()
                .filter(s -> s.getStatus() != null && s.getStatus().equals(ACTIVITY_STATUS_COMPLETED))
                .count(),1);
        assertEquals(activityService.getAllActivities().stream().filter(s -> s.getStatus() == null)
                .count(),1);
    }

    @Test
    public void testUpdateActivityWithException() {

        Activity justActivity = new Activity();
        justActivity.setId("5");
        justActivity.setName("justActiv");
        justActivity.setStartDate(null);
        justActivity.setEndDate(null);

        assertThrows(ServiceException.class, () ->activityService.updateActivities(List.of(justActivity)));
    }

    @Test
    public void testGetStatusOfActivitiesForGroup() {

        List<String> res = activityService.getStatusOfActivitiesForGroup(group1);
        assertEquals(activities.size(), res.size());
        assertTrue(res.contains("complete"+ASSIGNED));
        assertTrue(res.containsAll(List.of( "prog", "haveStat","justActiv")));
    }

    @Test
    public void testGetActivityByName() {

        assertEquals(activities.get(0), activityService.getActivityByName("complete"));
        assertEquals(null, activityService.getActivityByName("test"));
    }

    @Test
    public void testAddGroup() throws ServiceException {
        Activity justActivity = new Activity();
        justActivity.setId("5");
        justActivity.setName("test");
        justActivity.setStartDate(null);
        justActivity.setEndDate(null);

        assertEquals(activityService.addActivity(justActivity), justActivity);
        Mockito.doReturn(Optional.of(justActivity)).when(activityRepository).findByName(justActivity.getName());
        assertThrows(ServiceException.class, () -> activityService.addActivity(justActivity));
    }
}
