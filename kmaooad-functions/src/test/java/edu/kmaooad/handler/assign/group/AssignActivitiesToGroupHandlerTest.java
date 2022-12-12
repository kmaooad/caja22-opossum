package edu.kmaooad.handler.assign.group;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.impl.assign.group.AssignActivitiesToGroupHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.*;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AssignActivitiesToGroupHandlerTest {
    @InjectMocks
    private AssignActivitiesToGroupHandler assignActivitiesToGroupHandler;

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
        Map<String, Object> data = new HashMap<>();
        data.put(GroupConstants.GROUP_MAP_KEY, new Group());
        userSession = UserSession.builder()
                .chatId(1L)
                .data(data)
                .build();
        userRequest = UserRequest.builder().update(update).userSession(userSession).build();
    }

    @Test
    public void testIsNotApplicableConversationState() {
        message.setText("Test group");
        userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
        Assertions.assertFalse(assignActivitiesToGroupHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsApplicable() {
        message.setText("Test group");
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_ACTIVITY_ASSIGN_CHOICE);
        Assertions.assertTrue(assignActivitiesToGroupHandler.isApplicable(userRequest));
    }

    @Test
    public void testHandler() {
        BotApiMethod<?> result = new SendMessage();
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_ACTIVITY_ASSIGN_CHOICE);
        Mockito.doReturn(new Group()).when(groupService).getGroupByName(Mockito.anyString());
        Mockito.doReturn(new Group()).when(groupService).getGroupById(Mockito.any());
        Mockito.doReturn(new Activity()).when(activityService).getActivityByName(Mockito.anyString());
        Mockito.doReturn(new SendMessage()).when(telegramService).sendMessage(Mockito.anyLong(), Mockito.anyString());
        HandlerResponse handlerResponse = assignActivitiesToGroupHandler.handle(userRequest);

        Assertions.assertNull(handlerResponse.getResult());
        Assertions.assertTrue(handlerResponse.isSuccess());
        Assertions.assertEquals(userSession.getConversationState(), ConversationState.WAITING_FOR_GROUP_ACTIVITY_ASSIGN_CHOICE);
    }
}
