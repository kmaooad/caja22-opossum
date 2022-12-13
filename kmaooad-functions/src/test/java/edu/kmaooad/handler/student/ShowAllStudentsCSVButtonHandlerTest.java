package edu.kmaooad.handler.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.StudentConstants;
import edu.kmaooad.handler.impl.student.ShowAllStudentsCSVButtonHandler;
import edu.kmaooad.handler.impl.student.StudentButtonsHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.MassStudentsService;
import edu.kmaooad.service.StudentService;
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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShowAllStudentsCSVButtonHandlerTest {
    @InjectMocks
    private ShowAllStudentsCSVButtonHandler showAllStudentsCSVButtonHandler;

    @Mock
    private StudentService studentService;
    @Mock
    private TelegramService telegramService;
    @Mock
    private UserSessionService userSessionService;
    @Mock
    private StudentButtonsHandler studentButtonsHandler;
    @Mock
    private MassStudentsService massStudentsService;

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
        message.setText(StudentConstants.STUDENT_SHOW_ALL_BUTTON_LABEL);
        userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
        Assertions.assertFalse(showAllStudentsCSVButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsNotApplicableMessage() {
        message.setText("Not applicable");
        userSession.setConversationState(ConversationState.WAITING_FOR_STUDENT_ACTION_CHOICE);
        Assertions.assertFalse(showAllStudentsCSVButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsApplicable() {
        message.setText(StudentConstants.STUDENT_SHOW_CSV_BUTTON_LABEL);
        userSession.setConversationState(ConversationState.WAITING_FOR_STUDENT_ACTION_CHOICE);
        Assertions.assertTrue(showAllStudentsCSVButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testHandler() {
        Mockito.when(studentButtonsHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(null, true));
        HandlerResponse handlerResponse = showAllStudentsCSVButtonHandler.handle(userRequest);

        Assertions.assertNull(handlerResponse.getResult());
        Assertions.assertTrue(handlerResponse.isSuccess());
    }
}
