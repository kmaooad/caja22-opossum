package edu.kmaooad.service;

import edu.kmaooad.model.Group;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    //Get all groups from db
    public List<Group> getAllGroups(){
        return groupRepository.findAll();
    }

    // приймає валідну групу
    // повертає тру якщо додано
    // шукає в базі повтори за іменем
    public boolean addGroup(Group group) {

        Group found = groupRepository.findByName(group.getName());

        if (found != null) {
            return false;
        } else {
            groupRepository.save(group);
            return true;
        }
    }

    // поки що оновлює тільки назву групи
    // тру якщо оновило
    public boolean updateGroup(Group groupUpdate) {
        Optional<Group> group = groupRepository.findById(groupUpdate.getId());
        if (group.isPresent()) {
            Group groupFound = group.get();
            groupFound.setName(groupUpdate.getName());
            groupFound.setGrade(groupUpdate.getGrade());
            groupFound.setYear(groupUpdate.getYear());
            groupRepository.save(groupFound);
            return true;
        }
        return false;

    }

    public boolean deleteGroup(String groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            Group groupFound = group.get();
            groupRepository.delete(groupFound);
            return true;
        }
        return false;
    }

    // TODO: 11/18/2022 ??? чи вона взагалі потрібна
    // аналогічно до актівіті
// нема перевірки чи такий студент є в бд !!!!
    public boolean addStudentGroup(String studentId, String groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            Group groupPresent = group.get();
            List<String> students = groupPresent.getStudentIds();
            String studentFound = null;
            for (String a : students) {
                if (a.equals(studentId)) {
                    studentFound = a;
                }
            }
            if (studentFound != null) {
                return false;
            }
            students.add(studentId);
            groupPresent.setStudentIds(students);
            groupRepository.save(groupPresent);
            return true;
        }
        return false;
    }

    public boolean addActivityGroup(String activityId, String groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            Group groupPresent = group.get();
            List<String> activities = groupPresent.getActivities();
            String activityFound = null;
            for (String a : activities) {
                if (a.equals(activityId)) {
                    activityFound = a;
                }
            }
            if (activityFound != null) {
                return false;
            }
            activities.add(activityId);
            groupPresent.setActivities(activities);
            groupRepository.save(groupPresent);
            return true;
        }
        return false;
    }

    public boolean deleteStudentGroup(String studentId, String groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            Group groupPresent = group.get();
            List<String> studentIds = groupPresent.getStudentIds();
            String activityFound = null;
            for (String a : studentIds) {
                if (a.equals(studentId)) {
                    activityFound = a;
                }
            }
            if (activityFound == null) {
                return false;
            }
            studentIds.remove(activityFound);
            groupPresent.setStudentIds(studentIds);
            groupRepository.save(groupPresent);
            return true;
        }
        return false;
    }

    public boolean deleteActivityGroup(String activityId, String groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            Group groupPresent = group.get();
            List<String> activities = groupPresent.getActivities();
            String activityFound = null;
            for (String a : activities) {
                if (a.equals(activityId)) {
                    activityFound = a;
                }
            }
            if (activityFound == null) {
                return false;
            }
            activities.remove(activityFound);
            groupPresent.setActivities(activities);
            groupRepository.save(groupPresent);
            return true;
        }
        return false;
    }

}
