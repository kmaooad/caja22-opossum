package edu.kmaooad.telegram;

import edu.kmaooad.Dispatcher;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.UserSessionService;
import edu.kmaooad.telegram.StudentsBot;
import edu.kmaooad.telegram.TelegramConfig;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StudentsBotTest {
    @Test
    public void testOnWebhookUpdateReceivedValidInput() {
        TelegramConfig telegramConfig = mock(TelegramConfig.class);
        when(telegramConfig.getWebhookPath()).thenReturn("https://test-webhook.com");

        Dispatcher dispatcher = mock(Dispatcher.class);
        when(dispatcher.dispatch(any(UserRequest.class))).thenReturn(new HandlerResponse(null, false));

        UserSessionService userSessionService = new UserSessionService();
        StudentsBot studentsBot = new StudentsBot(telegramConfig, dispatcher, userSessionService);

        User user = new User();
        user.setId(1L);
        user.setFirstName("User");

        Chat chat = new Chat();
        chat.setId(1L);

        Message message = new Message();
        message.setFrom(user);
        message.setChat(chat);
        message.setText("Message");

        Update update = new Update();
        update.setMessage(message);

        BotApiMethod<?> result = studentsBot.onWebhookUpdateReceived(update);

        assertNull(result);
    }

    @Test
    public void testOnWebhookUpdateReceivedInvalidInput() {
        TelegramConfig telegramConfig = mock(TelegramConfig.class);
        when(telegramConfig.getWebhookPath()).thenReturn("https://test-webhook.com");

        StudentsBot studentsBot = new StudentsBot(telegramConfig, null, null);

        BotApiMethod<?> result = studentsBot.onWebhookUpdateReceived(new Update());

        assertNull(result);
    }
}
