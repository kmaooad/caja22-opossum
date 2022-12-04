package edu.kmaooad.handler;

import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
public class UserRequestHandlerTest {
    private UserSession userSession = UserSession.builder().chatId(1L).build();

    private Update update = new Update();
    @Spy
    private Message message = new Message();

    private UserRequest userRequest;

    @BeforeEach
    public void initTests(){
        update.setMessage(message);

        userRequest = UserRequest.builder().userSession(userSession).chatId(1L).update(update).build();
    }

    @Test
    public void testIsCommandFalseNotCommand(){
        final String commandLabel = "Label";
        Mockito.doReturn(false).when(message).isCommand();
        message.setText(commandLabel);

        Assertions.assertFalse(UserRequestHandler.isCommand(update, commandLabel));
    }

    @Test
    public void testIsCommandFalseText(){
        final String commandLabel = "Label";
        Mockito.doReturn(true).when(message).isCommand();
        message.setText("Not right label");

        Assertions.assertFalse(UserRequestHandler.isCommand(update, commandLabel));
    }

    @Test
    public void testIsCommandTrue(){
        final String commandLabel = "Label";
        Mockito.doReturn(true).when(message).isCommand();
        message.setText(commandLabel);

        Assertions.assertTrue(UserRequestHandler.isCommand(update, commandLabel));
    }
}
