package edu.kmaooad.telegram.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageHandler{
    public BotApiMethod<?> answerMessage(Message message){
        final String chatId = message.getChatId().toString();
//        logger.info(chatId == null ? "null" : chatId.toString());
        return new SendMessage(chatId, "Simple message");
    }
}
