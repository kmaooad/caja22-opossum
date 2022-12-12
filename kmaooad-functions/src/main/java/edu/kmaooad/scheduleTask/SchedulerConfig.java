package edu.kmaooad.scheduleTask;

import edu.kmaooad.service.ActivityService;
import edu.kmaooad.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@Slf4j
/**
 * Auto-update activities based on schedule
 * (i.e. when activity start date passes, mark it as ‘in progress’ for the group,
 * when activity end date passes, mark it as completed by the group)
 */
public class SchedulerConfig {

    @Autowired
    private ActivityService activityService;

    // job to auto-update statuses for activities each 12 hours
    @Scheduled(fixedDelay = 43200000)
    public void updateStatuses() throws ServiceException {
        activityService.updateStatuses();
    }
}
