package edu.kmaooad.service;

import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.kmaooad.constants.bot.GlobalConstants.ASSIGNED;

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
                    if (group.getActivities().contains(activity.getId())) {
                        return activity.getName() + " " + ASSIGNED;
                    } else return activity.getName();
                })
                .collect(Collectors.toList());
    }

    public List<String> getStatusOfActivitiesForStudent(Student student) {
        List<Activity> allActivities = activityRepository.findAll();
        return allActivities.stream()
                .map(activity -> {
                    if (student.getActivities().contains(activity.getId())) {
                        return activity.getName() + " " + ASSIGNED;
                    } else return activity.getName();
                })
                .collect(Collectors.toList());
    }

    public Activity getActivityByName(String name) {
        Optional<Activity> group = activityRepository.findByName(name);
        if (group.isPresent()) {
            return group.get();
        } else {
            log.warn("Group not found, name: " + name);
            return null;
        }
    }

    public List<Activity> updateActivities(List<Activity> activityList) throws ServiceException {
        for (Activity a : activityList) {
            Optional<Activity> activity = activityRepository.findById(a.getId());
            if (activity.isPresent()) {
                Activity activityFound = activity.get();
                activityFound.setName(a.getName());
                activityFound.setStatus(a.getStatus());
                activityFound.setStartDate(a.getStartDate());
                activityFound.setEndDate(a.getEndDate());
                activityRepository.save(activityFound);
            } else {
                throw new ServiceException("Failed to update activity: " + activity + " activity with such id does not exists in database");
            }
        }
        return activityList;
    }

    public Activity addActivity(Activity activity) throws ServiceException {

        Optional<Activity> found = activityRepository.findByName(activity.getName());

        if (found.isPresent()) {
            throw new ServiceException("Failed to add activity: " + activity + " activity with such values exists in database");

        } else {
            activityRepository.save(activity);
            return activity;
        }
    }


}
