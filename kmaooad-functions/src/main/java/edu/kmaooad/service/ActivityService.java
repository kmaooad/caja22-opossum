package edu.kmaooad.service;

import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.kmaooad.constants.bot.GlobalConstants.*;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;


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
        Optional<Activity> activity = activityRepository.findByName(name);
        if (activity.isPresent()) {
            return activity.get();
        } else {
            log.warn("Activity not found, name: " + name);
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

    public List<Activity> updateActivitiesSetStatus(List<Activity> activityList, String status) throws ServiceException {
        for (Activity a : activityList) {
            Optional<Activity> activity = activityRepository.findById(a.getId());
            if (activity.isPresent()) {
                Activity activityFound = activity.get();
                activityFound.setName(a.getName());
                activityFound.setStatus(status);
                activityFound.setStartDate(a.getStartDate());
                activityFound.setEndDate(a.getEndDate());
                activityRepository.save(activityFound);
            } else {
                throw new ServiceException("Failed to update activity: " + activity +
                        " activity with such id does not exists in database");
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

    public void updateStatuses() throws ServiceException {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());

        List<Activity> activities = getAllActivities().stream().filter(c ->
                c.getStartDate() != null || c.getEndDate() != null ||
                        (c.getStatus() != null && !c.getStatus().equals(ACTIVITY_STATUS_COMPLETED))
        ).collect(Collectors.toList());

        log.warn("BEFORE JOB:" + getAllActivities().toString());

        List<Activity> completed = activities.stream().filter(c ->
                (c.getStatus() == null || c.getStatus().equals(ACTIVITY_STATUS_IN_PROGRESS))
                        && c.getStartDate().isBefore(localDate) && localDate.isAfter(c.getEndDate()))
                .collect(Collectors.toList());

        activities.removeAll(completed);

        List<Activity> isProgress = activities.stream().filter(c -> c.getStatus() == null &&
                c.getStartDate().isBefore(localDate)
                && localDate.isBefore(c.getEndDate()))
                .collect(Collectors.toList());

        updateActivitiesSetStatus(completed, ACTIVITY_STATUS_COMPLETED);
        updateActivitiesSetStatus(isProgress, ACTIVITY_STATUS_IN_PROGRESS);

        log.warn("AFTER JOB :" + getAllActivities().toString());
    }

}
