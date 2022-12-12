package edu.kmaooad.handler.assign.group;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.impl.assign.group.SelectActivitiesToAssignToGroupHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.ActivityService;
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

import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SelectActivitieToGroupHandlerTest {
    @InjectMocks
    private SelectActivitiesToAssignToGroupHandler selectActivitiesToAssignToGroupHandler;

    @Mock
    private KeyboardHelper keyboardHelper;
    @Mock
    private GroupService groupService;
    @Mock
    private ActivityService activityService;
    @Mock
    private TelegramService telegramService;



    private Message message;
    private Update update;
    private UserSession userSession;
    private UserRequest userRequest;

    @BeforeEach
    public void initTests() {
        message = new Message();
        message.setText("Test group");
        update = new Update();
        update.setMessage(message);
        userSession = UserSession.builder()
                .chatId(1L)
                .data(new HashMap<>())
                .build();
        userRequest = UserRequest.builder().update(update).userSession(userSession).build();
    }

    @Test
    public void testIsNotApplicableConversationState() {
        message.setText("Test group");
        userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
        Assertions.assertFalse(selectActivitiesToAssignToGroupHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsApplicable() {
        message.setText("Test group");
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_TO_ASSIGN_CHOICE);
        Assertions.assertTrue(selectActivitiesToAssignToGroupHandler.isApplicable(userRequest));
    }

    @Test
    public void testHandler() {
        BotApiMethod<?> result = new SendMessage();
        Mockito.doReturn(new Group()).when(groupService).getGroupByName(Mockito.anyString());
        Mockito.doReturn(new SendMessage()).when(telegramService).sendMessage(Mockito.anyLong(), Mockito.anyString());
        HandlerResponse handlerResponse = selectActivitiesToAssignToGroupHandler.handle(userRequest);

        Assertions.assertNull(handlerResponse.getResult());
        Assertions.assertTrue(handlerResponse.isSuccess());
        Assertions.assertEquals(userSession.getConversationState(), ConversationState.WAITING_FOR_GROUP_ACTIVITY_ASSIGN_CHOICE);
    }
}
