package edu.kmaooad.service;

import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.repositories.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.kmaooad.constants.bot.GroupConstants.ASSIGNED;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private GroupService groupService;

    private final String splitter = " || ";


    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public List<String> getStatusOfActivitiesForGroup(Group group) {
        List<Activity> allActivities = activityRepository.findAll();
        return allActivities.stream()
                .map(activity -> {
                    if (group.getActivities().contains(activity)) {
                        return activity.getName() + ASSIGNED;
                    } else return activity.getName();
                })
                .collect(Collectors.toList());
    }

    public Activity getActivityByName(String name){
        Optional<Activity> group = activityRepository.findByName(name);
        if(group.isPresent()){
            return group.get();
        } else {
            log.warn("Group not found, name: " + name);
            return null;
        }
    }


}
