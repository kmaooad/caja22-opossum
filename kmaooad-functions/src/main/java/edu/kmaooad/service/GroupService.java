package edu.kmaooad.service;

import edu.kmaooad.model.Group;
import edu.kmaooad.repositories.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    //Get all groups from db
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    /**
     * @param id id of the group.
     * @return group with passed id, null - if not found.
     */
    public Group getGroupById(String id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            return group.get();
        } else {
            log.warn("Group not found, id: " + id);
            return null;
        }
    }

    public Group getGroupByName(String name) {
        Optional<Group> group = groupRepository.findByName(name);
        if (group.isPresent()) {
            return group.get();
        } else {
            log.warn("Group not found, name: " + name);
            return null;
        }
    }

    /**
     * @param group - group to add
     * @return group added, if already exists in database - throws exception
     */
    public Group addGroup(Group group) throws ServiceException {

        Optional<Group> found = groupRepository.findByName(group.getName());

        if (found.isPresent()) {
            throw new ServiceException("Failed to add group: " + group + " group with such values exists in database");
        } else {
            groupRepository.save(group);
            return group;
        }
    }

    /**
     * @param groupUpdate - group to update
     * @return group updated, if not found - throws exception
     */
    public Group updateGroup(Group groupUpdate) throws ServiceException {
        Optional<Group> group = groupRepository.findById(groupUpdate.getId());
        if (group.isPresent()) {
            Group groupFound = group.get();
            groupFound.setName(groupUpdate.getName());
            groupFound.setGrade(groupUpdate.getGrade());
            groupFound.setYear(groupUpdate.getYear());
            groupRepository.save(groupFound);
            return groupUpdate;
        } else {
            throw new ServiceException("Failed to update group: " + group + " group with such id does not exists in database");
        }
    }


    /**
     * @param groupId - group to add
     * @return group deleted, if not found - throws exception
     */
    public Group deleteGroup(String groupId) throws ServiceException {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            Group groupFound = group.get();
            groupRepository.delete(groupFound);
            return groupFound;
        }
        throw new ServiceException("Failed to delete group with id: " + groupId + " group with such id does not exists in database");

    }

    // TODO: 11/18/2022 ??? чи вона взагалі потрібна
    // аналогічно до актівіті
    // нема перевірки чи такий студент є в бд !!!!

    /**
     * @param studentId - student id to add
     * @param groupId   - group id to add to
     * @return student id added, if group not found or student exists in this group - throws exception
     */
    public String addStudentGroup(String studentId, String groupId) throws ServiceException {
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
                throw new ServiceException("Failed to add student with id: " + studentId + " to group with id: " + groupId + " student witch such id exists in this group in database");

            }
            students.add(studentId);
            groupPresent.setStudentIds(students);
            groupRepository.save(groupPresent);
            return studentId;
        }
        throw new ServiceException("Failed to add student with id: " + studentId + " to group with id: " + groupId + " group with such id doesn`t exist in database");

    }

    /**
     * @param activityAdd - activity  to add
     * @param groupId     - group id to add to
     * @return activity added, if group not found or activity exists in this group - throws exception
     */
    public String addActivityGroup(String activityAdd, String groupId) throws ServiceException {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            Group groupPresent = group.get();
            List<String> activities = groupPresent.getActivities();
            String activityFound = null;
            for (String a : activities) {
                if (a.equals(activityAdd)) {
                    activityFound = a;
                }
            }
            if (activityFound != null) {
                throw new ServiceException("Failed to add activity: " + activityAdd + " to group with id: " + groupId + " activity witch such id exists in this group in database");

            }
            activities.add(activityAdd);
            groupPresent.setActivities(activities);
            groupRepository.save(groupPresent);
            return activityAdd;
        }
        throw new ServiceException("Failed to add activity: " + activityAdd + " to group with id: " + groupId + " group with such id doesn`t exist in database");
    }

    /**
     * @param studentId - student id to delete
     * @param groupId   - group id to deleted from
     * @return student id deleted, if group not found or student doesn`t exist in this group - throws exception
     */
    public String deleteStudentGroup(String studentId, String groupId) throws ServiceException {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            Group groupPresent = group.get();
            List<String> studentIds = groupPresent.getStudentIds();
            String studentFound = null;
            for (String a : studentIds) {
                if (a.equals(studentId)) {
                    studentFound = a;
                }
            }
            if (studentFound == null) {
                throw new ServiceException("Failed to delete student with id: " + studentId + " to group with id: " + groupId + " student witch such id doesn`t exist in this group in database");
            }
            studentIds.remove(studentFound);
            groupPresent.setStudentIds(studentIds);
            groupRepository.save(groupPresent);
            return studentId;
        }
        throw new ServiceException("Failed to delete student with id: " + studentId + " to group with id: " + groupId + " group with such id doesn`t exist in database");

    }

    /**
     * @param activityDelete - activity id to delete
     * @param groupId        - group id to deleted from
     * @return activity deleted, if group not found or activity doesn`t exist in this group - throws exception
     */
    public String deleteActivityGroup(String activityDelete, String groupId) throws ServiceException {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            Group groupPresent = group.get();
            List<String> activities = groupPresent.getActivities();
            String activityFound = null;
            for (String a : activities) {
                if (a.equals(activityDelete)) {
                    activityFound = a;
                }
            }
            if (activityFound == null) {
                throw new ServiceException("Failed to delete activity with id: " + activityDelete + " to group with id: " + groupId + " activity witch such id doesn`t exist in this group in database");

            }
            activities.remove(activityFound);
            groupPresent.setActivities(activities);
            groupRepository.save(groupPresent);
            return activityDelete;
        }
        throw new ServiceException("Failed to delete activity with with id: " + activityDelete + " to group with id: " + groupId + " group with such id doesn`t exist in database");

    }

}
