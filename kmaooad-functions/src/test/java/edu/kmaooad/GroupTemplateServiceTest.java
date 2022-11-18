package edu.kmaooad;


import edu.kmaooad.service.GroupTemplateService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(MockitoExtension.class)
public class GroupTemplateServiceTest {


    @Autowired
    GroupTemplateService groupTemplateService;


    @Autowired
    private WebTestClient client;


    @Test
    public void test() throws Exception {
    }


}
