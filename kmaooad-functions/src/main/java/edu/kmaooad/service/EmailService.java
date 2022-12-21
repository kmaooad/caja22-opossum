package edu.kmaooad.service;

import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private void sendEmail(String email, String subject, String body) {

        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(msg);
        } catch (MessagingException | MailSendException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void notifyAboutCreationStudent(Student student) {
        sendEmail(student.getEmail(), "OpossumNotify. Вас було зареєстровано в наш бот",
                "<h2>" + student.getLastName() + " " + student.getFirstName() + " " + student.getPatronym() + ", </h2> \n" +
                        "<h3> Вас було зареєстровано в наш бот як Студента " + student.getDepartment() + "</h3>"
        );
    }

    public void notifyAboutAddedActivity(Student student, Activity activity){
        sendEmail(student.getEmail(), "OpossumNotify. Вам було додано активність " + activity.getName(),
                "<h2>" + student.getLastName() + " " + student.getFirstName() + " " + student.getPatronym() + "</h2>, \n" +
                        "<h3>Вам було додано активність " + activity.getName() + "</h3> \n "+
                        "<b> Дата початку: " + activity.getStartDate() + "</b>\n "+
                        "<b> Дата кінця: " + activity.getEndDate() + "</b>\n "+
                        "<b> Статус: " + activity.getStatus() + "</b>\n "
        );
    }
}
