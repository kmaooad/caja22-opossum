package edu.kmaooad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * The service is used to make mass operations with students.
 * Here we parse files, check whether they're valid and call another services.
 */
@Service
public class MassStudentsService {

    @Autowired
    @Qualifier("emailsender")
    private JavaMailSender mailSender;

    public void sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("opossum.bot.notify@gmail.com");
        message.setTo("at.cat.smile@gmail.com");
        message.setSubject("Hello from Spring Boot!");
        message.setText("This is an email sent using the Spring Framework's SimpleMailMessage class.");
        mailSender.send(message);
    }
}
