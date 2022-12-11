package edu.kmaooad.handler.assign.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.StudentConstants;
import edu.kmaooad.handler.impl.assign.student.AssignActivitiesToStudentHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.*;
import edu.kmaooad.service.ActivityService;
import edu.kmaooad.service.StudentService;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AssignActivitiesToStudentHandlerTest {
    @InjectMocks
    private AssignActivitiesToStudentHandler assignActivitiesToStudentHandler;

    @Mock
    private KeyboardHelper keyboardHelper;
    @Mock
    private StudentService studentService;
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
        message.setText("Test student");
        update = new Update();
        update.setMessage(message);
        Map<String, Object> data = new HashMap<>();
        data.put(StudentConstants.STUDENT_MAP_KEY, new Student());
        userSession = UserSession.builder()
                .chatId(1L)
                .data(data)
                .build();
        userRequest = UserRequest.builder().update(update).userSession(userSession).build();
    }

    @Test
    public void testIsNotApplicableConversationState() {
        message.setText("Test student");
        userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
        Assertions.assertFalse(assignActivitiesToStudentHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsApplicable() {
        message.setText("Test student");
        userSession.setConversationState(ConversationState.WAITING_FOR_STUDENT_ACTIVITY_ASSIGN_CHOICE);
        Assertions.assertTrue(assignActivitiesToStudentHandler.isApplicable(userRequest));
    }

    @Test
    public void testHandler() {
        BotApiMethod<?> result = new SendMessage();
        userSession.setConversationState(ConversationState.WAITING_FOR_STUDENT_ACTIVITY_ASSIGN_CHOICE);
        Mockito.doReturn(new Student()).when(studentService).getStudentByEmail(Mockito.any());
        Mockito.doReturn(new Activity()).when(activityService).getActivityByName(Mockito.anyString());
        Mockito.doReturn(new SendMessage()).when(telegramService).sendMessage(Mockito.anyLong(), Mockito.anyString());
        HandlerResponse handlerResponse = assignActivitiesToStudentHandler.handle(userRequest);

        Assertions.assertNull(handlerResponse.getResult());
        Assertions.assertTrue(handlerResponse.isSuccess());
        Assertions.assertEquals(userSession.getConversationState(), ConversationState.WAITING_FOR_STUDENT_ACTIVITY_ASSIGN_CHOICE);
    }
}
