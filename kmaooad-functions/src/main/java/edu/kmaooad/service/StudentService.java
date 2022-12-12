package edu.kmaooad.service;

import edu.kmaooad.model.Group;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.GroupRepository;
import edu.kmaooad.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;


    // приймає список унікальних валідних студентів

    /**
     * @param students - list of students to add
     * @return added students updated, if at least one already exists - throws exception
     */
    public List<Student> addStudents(List<Student> students) throws ServiceException {

        List<Student> studentsAdded = new ArrayList<>();
        List<Student> studentsNotAdded = new ArrayList<>();
        for (Student s : students) {
            Optional<Student> found = studentRepository.findByEmail(s.getEmail());
            if (found.isEmpty()) {
                studentsAdded.add(s);
            } else {
                throw new ServiceException("Failed to add students: contains student " + s + " already exists in database");
            }
        }
        studentRepository.saveAll(studentsAdded);
        return studentsAdded;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentByEmail(String email) {
        Optional<Student> group = studentRepository.findByEmail(email);
        if (group.isPresent()) {
            return group.get();
        } else {
            log.warn("Student not found, email: " + email);
            return null;
        }
    }

    // не додає якщо в групі студента вже є ця активність

    /**
     * @param activityAdd - activity  to add
     * @param studentId   - student id to add to
     * @return activity added, if student not found or activity exists in this student - throws exception
     */
    public String addStudentActivity(String activityAdd, String studentId) throws ServiceException {

        Optional<Student> student = studentRepository.findById(studentId);

        if (student.isPresent()) {
            Student studentPresent = student.get();
            List<String> activities = studentPresent.getActivities();
            for (String a : activities) {
                if (a.equals(activityAdd)) {
                    throw new ServiceException("Failed to add activity: " + activityAdd + " to student with id: " + studentId + " activity witch such id exists in this student in database");

                }
            }
            Optional<Group> group = groupRepository.findById(studentPresent.getGroupId());
            if (group.isPresent()) {
                Group groupPresent = group.get();
                List<String> activitiesInGroup = groupPresent.getActivities();
                for (String a : activitiesInGroup) {
                    if (a.equals(activityAdd)) {
                        throw new ServiceException("Failed to add activity: " + activityAdd + " to student with id: " + studentId + " student is assigned group with such activity");

                    }
                }
            }
            studentPresent.getActivities().add(activityAdd);
            studentRepository.save(studentPresent);
            return activityAdd;
        }
        throw new ServiceException("Failed to add activity: " + activityAdd + " to student with id: " + studentId + " student with such id doesn`t exist in database");

    }

    /**
     * @param activityId - activity id to delete
     * @param studentId  - student id to add to
     * @return activity id deleted, if student not found or activity doesn`t exist in this student - throws exception
     */
    public String deleteStudentActivity(String activityId, String studentId) throws ServiceException {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            Student studentPresent = student.get();
            List<String> activities = studentPresent.getActivities();
            String activityFound = null;
            for (String a : activities) {
                if (a.equals(activityId)) {
                    activityFound = a;
                }
            }
            if (activityFound == null) {
                throw new ServiceException("Failed to add activity with id: " + activityId + " to student with id: " + studentId + "activity witch such id doesn`t exist in this student in database");
            }
            activities.remove(activityFound);
            studentPresent.setActivities(activities);
            studentRepository.save(studentPresent);
            return activityId;
        }
        throw new ServiceException("Failed to delete activity with: " + activityId + " to student with id: " + studentId + " student with such id doesn`t exist in database");

    }


    // приймає список унікальних валідних студентів
    // повертає апдейтнутих студентів


    /**
     * @param students - list of students to update
     * @return added students updated, if at least one doesn`t exists - throws exception
     */
    public List<Student> updateStudents(List<Student> students) throws ServiceException {
        List<Student> updatedStudents = new ArrayList<>();
        List<Student> notUpdatedStudents = new ArrayList<>();
        for (Student s : students) {
            Optional<Student> student = studentRepository.findById(s.getId());
            if (student.isPresent()) {
                Student found = student.get();
                found.setEmail(s.getEmail());
                found.setFirstName(s.getFirstName());
                found.setDepartment(s.getDepartment());
                found.setGroupId(s.getGroupId());
                found.setPatronym(s.getPatronym());
                updatedStudents.add(s);
            } else {
                throw new ServiceException("Failed to update students: contains student " + s + " doesn`t exists in database");
            }

        }
        studentRepository.saveAll(updatedStudents);
        return updatedStudents;
    }

}
