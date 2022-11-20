package edu.kmaooad.service;

import edu.kmaooad.telegram.StudentsBot;
import edu.kmaooad.telegram.StudentsBotSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Arrays;

/**
 * Service for communication with Telegram API
 */
@Slf4j
@Component
public class TelegramService {
    private final StudentsBotSender studentsBotSender;

    public TelegramService(StudentsBotSender studentsBotSender) {
        this.studentsBotSender = studentsBotSender;
    }

    public BotApiMethod<?> sendMessage(Long chatId, String text) {
        return sendMessage(chatId, text, null);
    }

    public BotApiMethod<?> sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage
                .builder()
                .text(text)
                .chatId(chatId.toString())
                //Other possible parse modes: MARKDOWNV2, MARKDOWN, which allows to make text bold, and all other things
                .parseMode(ParseMode.HTML)
                .replyMarkup(replyKeyboard)
                .build();
        try {
            execute(sendMessage);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return sendMessage;
    }

    private void execute(BotApiMethod<?> botApiMethod) throws Exception {
        studentsBotSender.execute(botApiMethod);
    }
}
