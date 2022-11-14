package edu.kmaooad;


import edu.kmaooad.model.Activity;
import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.model.Student;
import edu.kmaooad.repositories.StudentRepository;
import edu.kmaooad.service.GroupTemplateService;
import edu.kmaooad.service.StudentService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@FunctionalSpringBootTest
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
public class GroupTemplateServiceTest {


    @Autowired
    GroupTemplateService groupTemplateService;


    @Autowired
    private WebTestClient client;


    @Test
    public void test() throws Exception {
    }


}
