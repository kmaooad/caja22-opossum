package edu.kmaooad.handler;

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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StartCommandHandlerTest {
    private StartCommandHandler startHandler;
    @Mock
    private TelegramService telegramService;

    private final KeyboardHelper keyboardHelper = new KeyboardHelper();

    @Spy
    private Message messageApplicable;
    private static final Update updateApplicable = new Update();

    private UserSessionService userSessionService = new UserSessionService();

    private static final UserSession userSession = UserSession.builder().chatId(1L).build();
    private static final UserRequest userRequest = UserRequest.builder().chatId(1L).userSession(userSession).build();
    private static final UserRequest userRequestApplicable = UserRequest.builder().chatId(1L).userSession(userSession).update(updateApplicable).build();

    private static final UserRequest userRequestNotApplicable = UserRequest.builder().chatId(1L).update(new Update()).build();

    private static final BotApiMethod<?> resultSendMessage = SendMessage.builder().chatId(1L).text("Request").build();

    @BeforeEach
    public void initTests() {
        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(resultSendMessage).when(telegramService).sendMessage(Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(ReplyKeyboard.class));
        Mockito.doReturn(resultSendMessage).when(telegramService).sendMessage(Mockito.any(Long.class), Mockito.any(String.class));
        Mockito.doReturn(true).when(messageApplicable).isCommand();

        messageApplicable.setText("/start");
        updateApplicable.setMessage(messageApplicable);

        startHandler = new StartCommandHandler(telegramService, keyboardHelper, userSessionService);
    }

    @Test
    public void startCommandHandlerTestHandle() {
        HandlerResponse result = startHandler.handle(userRequest);

        Assertions.assertEquals(resultSendMessage, result.getResult());
    }

    @Test
    public void testIsApplicable(){
        Assertions.assertTrue(startHandler.isApplicable(userRequestApplicable));
    }

    @Test
    public void testIsNotApplicable(){
        Assertions.assertFalse(startHandler.isApplicable(userRequestNotApplicable));
    }
}
