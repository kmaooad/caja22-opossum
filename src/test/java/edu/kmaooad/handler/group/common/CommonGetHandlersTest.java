package edu.kmaooad.handler.group.common;

import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.impl.group.common.*;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.telegram.StudentsBotSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommonGetHandlersTest {
    @InjectMocks
    private TelegramService telegramService;
    @Mock
    private StudentsBotSender studentsBotSender;

    private KeyboardHelper keyboardHelper = new KeyboardHelper();

    private UserSession userSession;
    private Message message;
    private Update update;
    private UserRequest userRequest;

    @BeforeEach
    public void initTests(){
        try {
            //Immediately return message
            Mockito.when(studentsBotSender.execute(Mockito.any(SendMessage.class))).thenAnswer(res -> res);
//            getGroupIDHandler = new GetGroupIDHandler();
//            getGroupNameHandler = new GetGroupNameHandler();
            userSession = UserSession.builder().chatId(1L).data(Map.of(GroupConstants.GROUP_MAP_KEY, new Group())).build();

            message = new Message();
            update = new Update();
            update.setMessage(message);

            userRequest = UserRequest.builder().userSession(userSession).update(update).chatId(1L).build();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testValidInputGetGradeHandler(){
        final int grade = 3;
        GetGroupGradeHandler getGroupGradeHandler = new GetGroupGradeHandler(telegramService, keyboardHelper);
        message.setText(Integer.toString(grade));

        HandlerResponse response = getGroupGradeHandler.handle(userRequest);

        Assertions.assertEquals(((Group)userRequest.getUserSession().getData().get(GroupConstants.GROUP_MAP_KEY)).getGrade(), grade);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testInvalidInputGetGradeHandler(){
        final String input = "Not valid grade";
        GetGroupGradeHandler getGroupGradeHandler = new GetGroupGradeHandler(telegramService, keyboardHelper);
        message.setText(input);

        HandlerResponse response = getGroupGradeHandler.handle(userRequest);

        Assertions.assertEquals(((SendMessage)response.getResult()).getText(), String.format(GroupConstants.WRONG_GRADE, input));
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    public void testValidInputGetYearHandler(){
        final int year = 2015;
        GetGroupYearHandler getGroupYearHandler = new GetGroupYearHandler(telegramService, keyboardHelper);
        message.setText(Integer.toString(year));

        HandlerResponse response = getGroupYearHandler.handle(userRequest);

        Assertions.assertEquals(((Group)userRequest.getUserSession().getData().get(GroupConstants.GROUP_MAP_KEY)).getYear(), year);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testInvalidInputGetYearHandler(){
        final String input = "Not valid year";
        GetGroupYearHandler getGroupYearHandler = new GetGroupYearHandler(telegramService, keyboardHelper);
        message.setText(input);

        HandlerResponse response = getGroupYearHandler.handle(userRequest);

        Assertions.assertEquals(((SendMessage)response.getResult()).getText(), String.format(GroupConstants.WRONG_YEAR, input));
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    public void testValidInputGetIDHandler(){
        final int input = 2015;
        GetGroupIDHandler getGroupIDHandler = new GetGroupIDHandler();
        message.setText(Integer.toString(input));

        HandlerResponse response = getGroupIDHandler.handle(userRequest);

        Assertions.assertEquals(((Group)userRequest.getUserSession().getData().get(GroupConstants.GROUP_MAP_KEY)).getId(), Integer.toString(input));
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testValidInputGetNameHandler(){
        final String input = "Some name";
        GetGroupNameHandler getGroupNameHandler = new GetGroupNameHandler();
        message.setText(input);

        HandlerResponse response = getGroupNameHandler.handle(userRequest);

        Assertions.assertEquals(((Group)userRequest.getUserSession().getData().get(GroupConstants.GROUP_MAP_KEY)).getName(), input);
        Assertions.assertTrue(response.isSuccess());
    }

}
