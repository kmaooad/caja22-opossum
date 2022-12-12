package edu.kmaooad.handler.assign.group;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.handler.impl.assign.group.SelectGroupToAssignHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
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
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SelectGroupToAssignHandlerTest {
    @InjectMocks
    private SelectGroupToAssignHandler selectGroupToAssignHandler;

    @Mock
    private KeyboardHelper keyboardHelper;
    @Mock
    private GroupService groupService;
    @Mock
    private TelegramService telegramService;

    private Message message;
    private Update update;
    private UserSession userSession;
    private UserRequest userRequest;

    @BeforeEach
    public void initTests() {
        message = new Message();
        update = new Update();
        update.setMessage(message);
        userSession = UserSession.builder()
                .chatId(1L)
                .build();
        userRequest = UserRequest.builder().update(update).userSession(userSession).build();
    }

    @Test
    public void testIsNotApplicableConversationState() {
        message.setText(GlobalConstants.GROUP_BUTTON_LABEL);
        userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
        Assertions.assertFalse(selectGroupToAssignHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsNotApplicableMessage() {
        message.setText("Not applicable");
        userSession.setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        Assertions.assertFalse(selectGroupToAssignHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsApplicable() {
        message.setText(GlobalConstants.GROUP_SHOW_ALL_ASSIGN_LABEL);
        userSession.setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        Assertions.assertTrue(selectGroupToAssignHandler.isApplicable(userRequest));
    }

    @Test
    public void testHandler() {
        BotApiMethod<?> result = new SendMessage();
        Mockito.doReturn(new SendMessage()).when(telegramService).sendMessage(Mockito.anyLong(), Mockito.anyString());
        HandlerResponse handlerResponse = selectGroupToAssignHandler.handle(userRequest);

        Assertions.assertNull(handlerResponse.getResult());
        Assertions.assertTrue(handlerResponse.isSuccess());
        Assertions.assertEquals(userSession.getConversationState(), ConversationState.WAITING_FOR_GROUP_TO_ASSIGN_CHOICE);
    }
}
