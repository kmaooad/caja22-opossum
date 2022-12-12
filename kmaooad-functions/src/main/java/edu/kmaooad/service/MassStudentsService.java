package edu.kmaooad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * The service is used to make mass operations with students.
 * Here we parse files, check whether they're valid and call another services.
 */
@Service
public class MassStudentsService {

    @Autowired
    private JavaMailSender mailSender;

    private final StudentService studentService;

    public MassStudentsService(StudentService studentService) {
        this.studentService = studentService;
    }
}
