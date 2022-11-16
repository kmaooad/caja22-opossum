package edu.kmaooad;

import edu.kmaooad.functions.TelegramWebhook;
import edu.kmaooad.telegram.StudentsBot;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TelegramWebhookTest {
    @Test
    public void testValidUpdate() {
        StudentsBot studentsBot = mock(StudentsBot.class);
        doReturn(null).when(studentsBot).onWebhookUpdateReceived(any(Update.class));
        BotApiMethod<?> result = new TelegramWebhook(studentsBot).apply(new Update());

        assertNull(result);
    }

    @Test
    public void testInvalidUpdate() {
        StudentsBot studentsBot = mock(StudentsBot.class);
        doThrow(new RuntimeException()).when(studentsBot).onWebhookUpdateReceived(any(Update.class));
        BotApiMethod<?> result = new TelegramWebhook(studentsBot).apply(new Update());

        System.out.println(result);
        assertNull(result);
    }
}
