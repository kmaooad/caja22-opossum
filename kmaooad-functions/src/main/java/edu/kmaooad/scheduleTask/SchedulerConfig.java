package edu.kmaooad.scheduleTask;

import edu.kmaooad.service.GroupService;
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
public class SchedulerConfig {

    @Autowired
    private GroupService groupService;

    @Scheduled(fixedDelay = 4000)
    public void computePrice() throws InterruptedException {
        log.warn("Hi I am job'a!");
//        log.warn("TEST: " +groupService.getGroupById("1").toString());
    }
}
