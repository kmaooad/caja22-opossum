package edu.kmaooad.handler.grouptemplate.common;

import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.impl.template.common.GetTemplateGradeHandler;
import edu.kmaooad.handler.impl.template.common.GetTemplateIdHandler;
import edu.kmaooad.handler.impl.template.common.GetTemplateNameHandler;
import edu.kmaooad.handler.impl.template.common.GetTemplateYearHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.*;
import edu.kmaooad.service.GroupTemplateService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.telegram.StudentsBotSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class GetTemplateHandlersTest {
    @InjectMocks
    private TelegramService telegramService;

    @Mock
    private GroupTemplateService groupTemplateService;

    @Mock
    private StudentsBotSender studentsBotSender;

    private KeyboardHelper keyboardHelper = new KeyboardHelper();

    private UserSession userSession;
    private Message message;
    private Update update;
    private UserRequest userRequest;

    @BeforeEach
    public void initTests() {
        userSession = UserSession.builder().chatId(1L).data(Map.of(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY, new GroupTemplate())).build();

        message = new Message();
        update = new Update();
        update.setMessage(message);
        userRequest = UserRequest.builder().userSession(userSession).update(update).chatId(1L).build();
    }

    @Test
    public void testValidInputGetIDHandler() {
        final String input = "id";
        GetTemplateIdHandler getTemplateIdHandler = new GetTemplateIdHandler(groupTemplateService, telegramService, keyboardHelper);
        message.setText(input);
        HandlerResponse response = getTemplateIdHandler.handle(userRequest);
        Assertions.assertEquals(((GroupTemplate)userRequest.getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY)).getId(), input);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testValidInputGetNameHandler() {
        final String input = "Some name";
        GetTemplateNameHandler getTemplateNameHandler = new GetTemplateNameHandler();
        message.setText(input);
        HandlerResponse response = getTemplateNameHandler.handle(userRequest);
        Assertions.assertEquals(((GroupTemplate)userRequest.getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY)).getName(), input);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testValidInputGetGradeHandler() {
        final int input = 4;
        GetTemplateGradeHandler getTemplateGradeHandler = new GetTemplateGradeHandler(telegramService, keyboardHelper);
        message.setText(Integer.toString(input));
        HandlerResponse response = getTemplateGradeHandler.handle(userRequest);
        Assertions.assertEquals(((GroupTemplate)userRequest.getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY)).getGrade(), input);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testValidInputGetYearHandler() {
        final int input = 2022;
        GetTemplateYearHandler getTemplateYearHandler = new GetTemplateYearHandler(telegramService, keyboardHelper);
        message.setText(Integer.toString(input));
        HandlerResponse response = getTemplateYearHandler.handle(userRequest);
        Assertions.assertEquals(((GroupTemplate)userRequest.getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY)).getYear(), input);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testSkippedInputGetNameHandler() {
        final String input = "-";
        GetTemplateNameHandler getTemplateNameHandler = new GetTemplateNameHandler();
        message.setText(input);
        HandlerResponse response = getTemplateNameHandler.handle(userRequest);
        Assertions.assertEquals(((GroupTemplate)userRequest.getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY)).getName(), null);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testSkippedInputGetGradeHandler() {
        final String input = "-";
        GetTemplateGradeHandler getTemplateGradeHandler = new GetTemplateGradeHandler(telegramService, keyboardHelper);
        message.setText(input);
        HandlerResponse response = getTemplateGradeHandler.handle(userRequest);
        Assertions.assertEquals(((GroupTemplate)userRequest.getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY)).getGrade(), null);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testSkippedInputGetYearHandler() {
        final String input = "-";
        GetTemplateYearHandler getTemplateYearHandler = new GetTemplateYearHandler(telegramService, keyboardHelper);
        message.setText(input);
        HandlerResponse response = getTemplateYearHandler.handle(userRequest);
        Assertions.assertEquals(((GroupTemplate)userRequest.getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY)).getYear(), null);
        Assertions.assertTrue(response.isSuccess());
    }
}
