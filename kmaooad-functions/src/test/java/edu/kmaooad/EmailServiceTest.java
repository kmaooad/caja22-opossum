package edu.kmaooad;

import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Student;
import edu.kmaooad.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mail.javamail.JavaMailSender;

import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmailServiceTest {
    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender mailSender;

    @BeforeEach
    public void initTests(){
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testNotifyAboutAddedActivity() {
        emailService.notifyAboutAddedActivity(new Student(), new Activity());
    }

    @Test
    public void test(){
        emailService.notifyAboutCreationStudent(new Student());
    }
}