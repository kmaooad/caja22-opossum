package edu.kmaooad;


import edu.kmaooad.service.GroupTemplateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GroupTemplateServiceTest {


    @Autowired
    GroupTemplateService groupTemplateService;


    @Autowired
    private WebTestClient client;


    @Test
    public void test() throws Exception {
    }
}
