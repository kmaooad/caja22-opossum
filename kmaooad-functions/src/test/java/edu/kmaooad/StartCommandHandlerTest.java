package edu.kmaooad;

import edu.kmaooad.handler.impl.StartCommandHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.TelegramService;
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

    private static final UserRequest userRequest = UserRequest.builder().chatId(1L).build();

    @Spy
    private Message messageApplicable;
    private static final Update updateApplicable = new Update();
    private static final UserRequest userRequestApplicable = UserRequest.builder().chatId(1L).update(updateApplicable).build();

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

        startHandler = new StartCommandHandler(telegramService, keyboardHelper);
    }

    @Test
    public void startCommandHandlerTestHandle() {
        BotApiMethod result = startHandler.handle(userRequest);

        Assertions.assertEquals(resultSendMessage, result);
    }

    @Test
    public void testIsApplicable(){
        Assertions.assertTrue(startHandler.isApplicable(userRequestApplicable));
    }

    @Test
    public void testIsNotApplicable(){
        Assertions.assertFalse(startHandler.isApplicable(userRequestNotApplicable));
    }

    @Test
    public void shouldBeGlobal(){
        Assertions.assertTrue(startHandler.isGlobal());
    }
}
