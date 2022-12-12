package edu.kmaooad;


import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.repositories.GroupTemplateRepository;
import edu.kmaooad.service.GroupTemplateService;
import edu.kmaooad.service.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GroupTemplateServiceTest {

/*
    @Autowired
    GroupTemplateService groupTemplateService;


    @Autowired
    private WebTestClient client;
*/

    @InjectMocks
    GroupTemplateService groupTemplateService;

    @Mock
    GroupTemplateRepository groupTemplateRepository;

    final GroupTemplate group1 = new GroupTemplate();
    final String group1Name = "group1";
    final String group1ID = "1";
    final String missingID = "2";
    final Optional<Group> missingGroup = Optional.empty();
    final String student1ID = "1";
    final Activity activity = new Activity();

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        group1.setId(group1ID);
        group1.setName("group1");
        group1.setYear(2022);
        group1.setGrade(1);

        activity.setId("1");


        Mockito.doReturn(Optional.of(group1)).when(groupTemplateRepository).findById(group1ID);
        Mockito.doReturn(missingGroup).when(groupTemplateRepository).findById(missingID);
    }


    @Test
    public void addGroup() throws ServiceException {
        assertEquals(group1, groupTemplateService.addGroupTemplate(group1));
        Mockito.doReturn(group1).when(groupTemplateRepository).findByNameAndYearAndGrade(group1.getName(), group1.getYear(), group1.getGrade());
        assertThrows(ServiceException.class, () -> groupTemplateService.addGroupTemplate(group1));

    }

    @Test
    public void updateGroup() throws ServiceException {

        group1.setName("group2");
        group1.setYear(2023);
        group1.setGrade(2);
        assertEquals(group1, groupTemplateService.updateGroupTemplate(group1));
        GroupTemplate group2 = new GroupTemplate();
        group2.setId(missingID);
        assertThrows(ServiceException.class, () -> groupTemplateService.updateGroupTemplate(group2));

    }

    @Test
    public void deleteGroup() throws ServiceException {
        assertEquals(group1ID, groupTemplateService.deleteGroupTemplate(group1ID).getId());
        assertThrows(ServiceException.class, () -> groupTemplateService.deleteGroupTemplate(missingID));
    }
}
