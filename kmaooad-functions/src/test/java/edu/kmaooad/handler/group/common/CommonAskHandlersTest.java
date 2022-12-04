package edu.kmaooad.handler.group.common;

import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.impl.group.common.AskGroupGradeHandler;
import edu.kmaooad.handler.impl.group.common.AskGroupIDHandler;
import edu.kmaooad.handler.impl.group.common.AskGroupNameHandler;
import edu.kmaooad.handler.impl.group.common.AskGroupYearHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ExtendWith(MockitoExtension.class)
public class CommonAskHandlersTest {
    @InjectMocks
    private TelegramService telegramService;
    @Mock
    private StudentsBotSender studentsBotSender;

    private KeyboardHelper keyboardHelper = new KeyboardHelper();

    private AskGroupGradeHandler askGroupGradeHandler;
    private AskGroupIDHandler askGroupIDHandler;
    private AskGroupNameHandler askGroupNameHandler;
    private AskGroupYearHandler askGroupYearHandler;

    private UserRequest userRequest = UserRequest.builder().chatId(1L).build();

    @BeforeEach
    public void initTests(){
        try {
            //Immediately return message
            Mockito.when(studentsBotSender.execute(Mockito.any(SendMessage.class))).thenAnswer(res -> res);
            askGroupGradeHandler = new AskGroupGradeHandler(telegramService, keyboardHelper);
            askGroupIDHandler = new AskGroupIDHandler(telegramService, keyboardHelper);
            askGroupNameHandler = new AskGroupNameHandler(telegramService, keyboardHelper);
            askGroupYearHandler = new AskGroupYearHandler(telegramService, keyboardHelper);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAskGradeHandler(){
        HandlerResponse response = askGroupGradeHandler.handle(userRequest);

        Assertions.assertEquals(((SendMessage)response.getResult()).getText(), GroupConstants.ASK_FOR_GROUP_GRADE);
    }

    @Test
    public void testAskYearHandler(){
        HandlerResponse response = askGroupYearHandler.handle(userRequest);

        Assertions.assertEquals(((SendMessage)response.getResult()).getText(), GroupConstants.ASK_FOR_GROUP_YEAR);
    }

    @Test
    public void testAskIDHandler(){
        HandlerResponse response = askGroupIDHandler.handle(userRequest);

        Assertions.assertEquals(((SendMessage)response.getResult()).getText(), GroupConstants.ASK_FOR_GROUP_ID);
    }
    @Test
    public void testAskNameHandler(){
        HandlerResponse response = askGroupNameHandler.handle(userRequest);

        Assertions.assertEquals(((SendMessage)response.getResult()).getText(), GroupConstants.ASK_FOR_GROUP_NAME);
    }
}
