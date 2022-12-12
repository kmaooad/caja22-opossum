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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ActivityServiceTest {


    @InjectMocks
    ActivityService activityService;

    @Mock
    ActivityRepository activityRepository;

    final Activity activity1 = new Activity();
    final String group1Name = "activity1";
    final String group1ID = "1";
    final String missingID = "2";
    final Optional<Activity> missingActivity = Optional.empty();
    final Activity activity = new Activity();

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        activity1.setId(group1ID);
        activity1.setName("activity1");
        activity1.setStatus("");
        activity1.setStartDate(new Date());
        activity1.setEndDate(new Date());

        activity.setId("1");
        Mockito.doReturn(Optional.of(activity1)).when(activityRepository).findById(group1ID);
        Mockito.doReturn(missingActivity).when(activityRepository).findById(missingID);
    }

    @Test
    public void addGroup() throws ServiceException {
        assertEquals(activityService.addActivity(activity1), activity1);
        Mockito.doReturn(Optional.of(activity1)).when(activityRepository).findByName(group1Name);
        assertThrows(ServiceException.class, () -> activityService.addActivity(activity1));
    }

    @Test
    public void updateGroup() throws ServiceException {
        activity1.setName("activity2");
        activity1.setStatus("");
        activity1.setStartDate(new Date());
        activity1.setEndDate(new Date());
        assertEquals(activityService.updateActivities(List.of(activity1)).get(0), activity1);
        Activity activity2 = new Activity();
        activity2.setId(missingID);
        assertThrows(ServiceException.class, () -> activityService.updateActivities(List.of(activity2)));

    }


}
