package edu.kmaooad.service;

import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.GroupRepository;
import edu.kmaooad.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;


    // приймає список унікальних валідних студентів
    // повертає недоданих студентів
    public List<Student> addStudents(List<Student> students) {

        List<Student> studentsAdded = new ArrayList<>();
        List<Student> studentsNotAdded = new ArrayList<>();
        for (Student s : students) {
            Student found = studentRepository.findByEmail(s.getEmail());
            if (found == null) {
                studentsAdded.add(s);

            } else {
                studentsNotAdded.add(s);
            }
        }
        studentRepository.saveAll(studentsAdded);
        return studentsNotAdded;
    }


    // повертає true якщо актівіті додано
// не додає якщо в групі студента вже є ця активність
    public boolean addStudentActivity(String studentId, String activityId) {

        Optional<Student> student = studentRepository.findById(studentId);

        if (student.isPresent()) {
            Student studentPresent = student.get();
            List<String> activities = studentPresent.getActivities();
            for (String a : activities) {
                if (a.equals(activityId)) {
                    return false;
                }
            }
            Optional<Group> group = groupRepository.findById(studentPresent.getGroupId());
            if (group.isPresent()) {
                Group groupPresent = group.get();
                List<String> activitiesInGroup = groupPresent.getActivities();
                for (String a : activitiesInGroup) {
                    if (a.equals(activityId)) {
                        return false;
                    }
                }
            }
            studentPresent.getActivities().add(activityId);
            studentRepository.save(studentPresent);
            return true;
        }
        return false;
    }

    // повертає true якщо актівіті видалено
    public boolean deleteStudentActivity(String studentId, String activityId) {
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
                return false;
            }
            activities.remove(activityFound);
            studentPresent.setActivities(activities);
            studentRepository.save(studentPresent);
            return true;
        }
        return false;
    }


    // приймає список унікальних валідних студентів
    // повертає неапдейтнутих студентів
    public List<Student> updateStudents(List<Student> students) {
        List<Student> updatedStudents = new ArrayList<>();
        List<Student> notUpdatedStudents = new ArrayList<>();
        for (Student s : students) {
            Optional<Student> student = studentRepository.findById(s.getId());
            if (student.isPresent()) {
                Student found = student.get();
                if (found != null) {
                    found.setEmail(s.getEmail());
                    found.setFirstName(s.getFirstName());
                    found.setDepartment(s.getDepartment());
                    found.setGroupId(s.getGroupId());
                    found.setPatronym(s.getPatronym());
                    updatedStudents.add(s);
                }
            } else {
                notUpdatedStudents.add(s);
            }

        }
        studentRepository.saveAll(updatedStudents);
        return notUpdatedStudents;
    }

}
