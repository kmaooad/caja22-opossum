package edu.kmaooad;

import edu.kmaooad.service.TelegramService;
import edu.kmaooad.telegram.StudentsBotSender;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TelegramServiceTest {

    @Test
    public void testSendValidMessage() throws Exception{
        StudentsBotSender studentsBotSender = mock(StudentsBotSender.class);
        when(studentsBotSender.execute(any(BotApiMethod.class))).thenReturn(null);
        TelegramService telegramService = new TelegramService(studentsBotSender);
        String text = "Some text";
        Long chatId = 1L;

        BotApiMethod<?> result = telegramService.sendMessage(chatId,  text);

        SendMessage actual = SendMessage
                .builder()
                .text(text)
                .chatId(chatId.toString())
                .parseMode(ParseMode.HTML)
                .build();

        assertEquals(result, actual);
    }

    @Test
    public void testSendInvalidMessage() throws Exception{
        StudentsBotSender studentsBotSender = mock(StudentsBotSender.class);
        when(studentsBotSender.execute(any(BotApiMethod.class))).thenThrow(new RuntimeException());
        TelegramService telegramService = new TelegramService(studentsBotSender);
        String text = "Some text";
        Long chatId = 1L;

        BotApiMethod<?> result = telegramService.sendMessage(chatId,  text);

        SendMessage actual = SendMessage
                .builder()
                .text(text)
                .chatId(chatId.toString())
                .parseMode(ParseMode.HTML)
                .build();

        assertEquals(result, actual);
    }
}
