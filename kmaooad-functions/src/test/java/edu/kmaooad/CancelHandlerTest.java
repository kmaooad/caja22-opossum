package edu.kmaooad;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.impl.CancelHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@ExtendWith(MockitoExtension.class)
public class CancelHandlerTest {
    private CancelHandler cancelHandler;

    @Mock
    private TelegramService telegramService;
    private KeyboardHelper keyboardHelper = new KeyboardHelper();
    private UserSessionService userSessionService = new UserSessionService();

    @Before
    public void initTests() {
        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(null).when(telegramService).sendMessage(Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(ReplyKeyboard.class));
        cancelHandler = new CancelHandler(telegramService, keyboardHelper, userSessionService);
    }

    @Test
    public void testHandleMethod() {
        UserSession session = UserSession.builder().state(ConversationState.WAITING_FOR_TEXT).build();
        cancelHandler.handle(UserRequest.builder().userSession(session).build());

        Assertions.assertEquals(session.getState(), ConversationState.CONVERSATION_STARTED);
    }

    @Test
    public void testIsApplicable(){
        Message messageApplicable = new Message();
        messageApplicable.setText("❌ Скасувати");

        Update updateApplicable = new Update();
        updateApplicable.setMessage(messageApplicable);

        UserRequest userRequestApplicable = UserRequest.builder().chatId(1L).update(updateApplicable).build();

        Assertions.assertTrue(cancelHandler.isApplicable(userRequestApplicable));
    }
}
