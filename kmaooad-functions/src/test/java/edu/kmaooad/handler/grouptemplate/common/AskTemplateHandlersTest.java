package edu.kmaooad.handler.grouptemplate.common;

import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.impl.template.common.*;
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
public class AskTemplateHandlersTest {
    @InjectMocks
    private TelegramService telegramService;
    @Mock
    private StudentsBotSender studentsBotSender;

    private KeyboardHelper keyboardHelper = new KeyboardHelper();

    private AskTemplateIdHandler askTemplateIdHandler;
    private AskTemplateNameHandler askTemplateNameHandler;
    private AskTemplateGradeHandler askTemplateGradeHandler;
    private AskTemplateYearHandler askTemplateYearHandler;

    private UserRequest userRequest = UserRequest.builder().chatId(1L).build();

    @BeforeEach
    public void initTests(){
        try {
            //Immediately return message
            Mockito.when(studentsBotSender.execute(Mockito.any(SendMessage.class))).thenAnswer(res -> res);
            askTemplateIdHandler = new AskTemplateIdHandler(telegramService, keyboardHelper);
            askTemplateNameHandler = new AskTemplateNameHandler(telegramService, keyboardHelper);
            askTemplateGradeHandler = new AskTemplateGradeHandler(telegramService, keyboardHelper);
            askTemplateYearHandler = new AskTemplateYearHandler(telegramService, keyboardHelper);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAskGradeHandler(){
        HandlerResponse response = askTemplateGradeHandler.handle(userRequest);
        Assertions.assertEquals(((SendMessage)response.getResult()).getText(),
                GroupTemplateConstants.ASK_FOR_GROUP_TEMPLATE_GRADE + "\n\n"
                        + GlobalConstants.SKIP_STEP);
    }

    @Test
    public void testAskYearHandler(){
        HandlerResponse response = askTemplateYearHandler.handle(userRequest);
        Assertions.assertEquals(((SendMessage)response.getResult()).getText(),
                GroupTemplateConstants.ASK_FOR_GROUP_TEMPLATE_YEAR + "\n\n"
                        + GlobalConstants.SKIP_STEP);
    }

    @Test
    public void testAskIDHandler(){
        HandlerResponse response = askTemplateIdHandler.handle(userRequest);
        Assertions.assertEquals(((SendMessage)response.getResult()).getText(),
                GroupTemplateConstants.ASK_FOR_GROUP_TEMPLATE_ID);
    }
    @Test
    public void testAskNameHandler(){
        HandlerResponse response = askTemplateNameHandler.handle(userRequest);
        Assertions.assertEquals(((SendMessage)response.getResult()).getText(),
                GroupTemplateConstants.ASK_FOR_GROUP_TEMPLATE_NAME + "\n\n"
                        + GlobalConstants.SKIP_STEP);
    }
}
