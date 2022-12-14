package edu.kmaooad.handler.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.impl.student.GetAllStudentsCSVButtonHandler;
import edu.kmaooad.handler.impl.student.StudentButtonsHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.MassStudentsService;
import edu.kmaooad.service.ServiceCSVException;
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

import static edu.kmaooad.service.ServiceCSVException.TypeOfCSVException.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAllStudentsCSVButtonHandlerTest {
    @InjectMocks
    private GetAllStudentsCSVButtonHandler getAllStudentsCSVButtonHandler;

    @Mock
    private TelegramService telegramService;
    @Mock
    private KeyboardHelper keyboardHelper;
    @Mock
    private MassStudentsService massStudentsService;
    @Mock
    private StudentService studentService;

    @Mock
    private StudentButtonsHandler studentButtonsHandler;

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
        message.setText("Some text");
        userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
        Assertions.assertFalse(getAllStudentsCSVButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsApplicable() {
        message.setText("Some text");
        userSession.setConversationState(ConversationState.WAITING_FOR_STUDENT_CSV);
        Assertions.assertTrue(getAllStudentsCSVButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testHandler() {
        BotApiMethod<?> result = new SendMessage();
        Mockito.doReturn(new SendMessage()).when(telegramService).sendMessage(Mockito.anyLong(), Mockito.anyString());
        HandlerResponse handlerResponse = getAllStudentsCSVButtonHandler.handle(userRequest);

        Assertions.assertEquals(userSession.getConversationState(), ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
    }


    @Test
    public void exceptionNotEnoughParamsThrowTestHandler() throws ServiceCSVException {
        Mockito.doThrow(new ServiceCSVException(NOT_ENOUGH_PARAMS_ON_LINE, "")).when(massStudentsService).parseStudentCSV(any());
        BotApiMethod<?> result = new SendMessage();
        Mockito.doReturn(new SendMessage()).when(telegramService).sendMessage(Mockito.anyLong(), Mockito.anyString());
        HandlerResponse handlerResponse = getAllStudentsCSVButtonHandler.handle(userRequest);

        Assertions.assertEquals(userSession.getConversationState(), ConversationState.WAITING_FOR_STUDENT_CSV);
    }

    @Test
    public void exceptionEmailDuplicateThrowTestHandler() throws ServiceCSVException {
        Mockito.doThrow(new ServiceCSVException(EMAIL_DUPLICATE, "")).when(massStudentsService).parseStudentCSV(any());
        BotApiMethod<?> result = new SendMessage();
        Mockito.doReturn(new SendMessage()).when(telegramService).sendMessage(Mockito.anyLong(), Mockito.anyString());
        HandlerResponse handlerResponse = getAllStudentsCSVButtonHandler.handle(userRequest);

        Assertions.assertEquals(userSession.getConversationState(), ConversationState.WAITING_FOR_STUDENT_CSV);
    }

    @Test
    public void exceptionActivityThrowTestHandler() throws ServiceCSVException {
        Mockito.doThrow(new ServiceCSVException(ACTIVITY_DOESNT_EXIST, "")).when(massStudentsService).parseStudentCSV(any());
        BotApiMethod<?> result = new SendMessage();
        Mockito.doReturn(new SendMessage()).when(telegramService).sendMessage(Mockito.anyLong(), Mockito.anyString());
        HandlerResponse handlerResponse = getAllStudentsCSVButtonHandler.handle(userRequest);

        Assertions.assertEquals(userSession.getConversationState(), ConversationState.WAITING_FOR_STUDENT_CSV);
    }

    @Test
    public void exceptionThrowTestHandler() throws ServiceCSVException {
        Mockito.doThrow(new RuntimeException()).when(massStudentsService).parseStudentCSV(any());
        BotApiMethod<?> result = new SendMessage();
        Mockito.doReturn(new SendMessage()).when(telegramService).sendMessage(Mockito.anyLong(), Mockito.anyString());
        HandlerResponse handlerResponse = getAllStudentsCSVButtonHandler.handle(userRequest);

        Assertions.assertEquals(userSession.getConversationState(), ConversationState.WAITING_FOR_STUDENT_CSV);
    }
}
