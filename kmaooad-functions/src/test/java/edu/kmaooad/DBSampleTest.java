package edu.kmaooad;

import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.repositories.GroupRepository;
import edu.kmaooad.repositories.GroupTemplateRepository;
import edu.kmaooad.service.GroupTemplateService;
import edu.kmaooad.service.ServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DBSampleTest {

    @Autowired
    GroupTemplateService groupTemplateService;

    @Test
    public void test() {
        GroupTemplate gr = new GroupTemplate();
        gr.setName("name");
        gr.setYear(2);
        gr.setGrade(1);
        gr.setId("639115d196a56063c9b7a655");
        try {
           // groupTemplateService.updateGroupTemplate(gr);
            groupTemplateService.updateGroupTemplate(gr);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //System.out.println(groupRepository.findAll());
    }
}
