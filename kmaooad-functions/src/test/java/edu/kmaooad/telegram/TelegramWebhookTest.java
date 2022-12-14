package edu.kmaooad.telegram;

import edu.kmaooad.functions.TelegramWebhook;
import edu.kmaooad.telegram.StudentsBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TelegramWebhookTest {

    @Mock
    StudentsBot studentsBot;

    @InjectMocks
    TelegramWebhook telegramWebhook;

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidUpdate() {
        lenient().doReturn(null).when(studentsBot).onWebhookUpdateReceived(any(Update.class));
        BotApiMethod<?> result = telegramWebhook.apply(new Update());

        assertNull(result);
    }

    @Test
    public void testInvalidUpdate() {
        lenient().doThrow(new RuntimeException()).when(studentsBot).onWebhookUpdateReceived(any(Update.class));
        BotApiMethod<?> result = new TelegramWebhook(studentsBot).apply(new Update());

        System.out.println(result);
        assertNull(result);
    }
}
