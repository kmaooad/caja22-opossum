package edu.kmaooad.handler.group.buttons;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.impl.group.GroupButtonsHandler;
import edu.kmaooad.handler.impl.group.ShowAllGroupsButtonHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShowAllGroupsButtonHandlerTest {
    @InjectMocks
    private ShowAllGroupsButtonHandler showAllGroupsButtonHandler;

    @Mock
    private GroupService groupService;
    @Mock
    private TelegramService telegramService;
    @Mock
    private UserSessionService userSessionService;
    @Mock
    private GroupButtonsHandler groupButtonsHandler;

    private Message message;
    private Update update;
    private UserSession userSession;
    private UserRequest userRequest;

    @BeforeEach
    public void initTests(){
        message = new Message();
        update = new Update();
        update.setMessage(message);
        userSession = UserSession.builder()
                .chatId(1L)
                .build();
        userRequest = UserRequest.builder().update(update).userSession(userSession).build();
    }

    @Test
    public void testIsNotApplicableConversationState(){
        message.setText(GroupConstants.GROUP_SHOW_ALL_BUTTON_LABEL);
        userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
        Assertions.assertFalse(showAllGroupsButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsNotApplicableMessage(){
        message.setText("Not applicable");
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE);
        Assertions.assertFalse(showAllGroupsButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsApplicable(){
        message.setText(GroupConstants.GROUP_SHOW_ALL_BUTTON_LABEL);
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE);
        Assertions.assertTrue(showAllGroupsButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testHandler(){
        Mockito.when(groupButtonsHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(null, true));
        HandlerResponse handlerResponse = showAllGroupsButtonHandler.handle(userRequest);

        Assertions.assertNull(handlerResponse.getResult());
        Assertions.assertTrue(handlerResponse.isSuccess());
    }
}
