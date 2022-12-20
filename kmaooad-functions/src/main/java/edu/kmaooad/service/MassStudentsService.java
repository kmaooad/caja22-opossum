package edu.kmaooad.service;

import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static edu.kmaooad.service.ServiceCSVException.TypeOfCSVException.EMAIL_DUPLICATE;
import static edu.kmaooad.service.ServiceCSVException.TypeOfCSVException.NOT_ENOUGH_PARAMS_ON_LINE;

/**
 * The service is used to make mass operations with students.
 * Here we parse files, check whether they're valid and call another services.
 */
@Service
@Slf4j
public class MassStudentsService {
    private final ActivityService activityService;

    public MassStudentsService(ActivityService activityService) {
        this.activityService = activityService;
    }

    public String generateStudentCSV(List<Student> students) {
        return students.stream()
                .map(student -> String.join(",",
                        student.getLastName(), student.getFirstName(), student.getPatronym(),
                        student.getEmail(),
                        student.getDepartment(),
                        String.join(",", student.getActivities())
                ))
                .collect(Collectors.joining("\n"));
    }

    public List<Student> parseStudentCSV(String message) throws ServiceCSVException {
        final int numberOfSingleStudentParams = 5;
        String[] lines = message.trim().split("\n");
        List<Student> students;
        try {
            students = Arrays.stream(lines)
                    .map((strStudent) -> {
                        log.info("Got student parameters (line): " + strStudent);
                        String[] studentParams = strStudent.trim().split(",");
                        log.info("Got student parameters: " + Arrays.toString(studentParams));
                        if (studentParams.length < numberOfSingleStudentParams) {
                            log.warn("Not enough parameters (" + Arrays.toString(studentParams) + " out of " + numberOfSingleStudentParams + ")");
                            throw new RuntimeException(strStudent);
                        }
                        int i = 0;
                        Student student = new Student();
                        student.setLastName(studentParams[i++]);
                        student.setFirstName(studentParams[i++]);
                        student.setPatronym(studentParams[i++]);
                        student.setEmail(studentParams[i++]);
                        student.setDepartment(studentParams[i++]);
                        log.info("Created (without activities)" + student);
                        Set<String> activitySet = new HashSet<>(studentParams.length - numberOfSingleStudentParams);
                        activitySet.addAll(Arrays.asList(studentParams).subList(numberOfSingleStudentParams, studentParams.length));
                        log.info("Student's activities before check " + activitySet);
                        student.setActivities(activitySet.stream().filter(activityService::existById).collect(Collectors.toList()));
                        log.info("Student's activities after check " + student.getActivities());
                        return student;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Catched " + e);
            throw new ServiceCSVException(NOT_ENOUGH_PARAMS_ON_LINE, e.getMessage());
        }

        Set<String> emails = new HashSet<>();
        Optional<Student> duplicateEmail = students.stream()
                .filter(st -> !emails.add(st.getEmail())).findFirst();
        if (duplicateEmail.isPresent()) {
            log.warn("Found dublicate email at student " + duplicateEmail.get());
            throw new ServiceCSVException(EMAIL_DUPLICATE,
                    lines[students.indexOf(duplicateEmail.get())]);
        }
        log.info("No email duplicates found");
        return students;
    }


}
