package edu.kmaooad.handler;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.impl.CancelHandler;
import edu.kmaooad.handler.impl.StartCommandHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CancelHandlerTest {
    private CancelHandler cancelHandler;

    @Mock
    private StartCommandHandler startCommandHandler;

    @BeforeEach
    public void initTests() {
        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(null).when(startCommandHandler).handle(Mockito.any(UserRequest.class));
        cancelHandler = new CancelHandler(startCommandHandler);
    }

    @Test
    public void testHandleMethod() {
        UserSession session = UserSession.builder().conversationState(ConversationState.WAITING_FOR_TEXT).build();
        cancelHandler.handle(UserRequest.builder().userSession(session).build());

        Assertions.assertEquals(session.getConversationState(), ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
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
