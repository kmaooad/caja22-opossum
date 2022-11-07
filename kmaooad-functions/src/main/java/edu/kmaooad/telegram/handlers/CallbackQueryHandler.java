package edu.kmaooad.telegram.handlers;

import edu.kmaooad.telegram.StudentsBot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CallbackQueryHandler {
    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery){
        final String chatId = buttonQuery.getMessage().getChatId().toString();

        return new SendMessage(chatId, "Callback");
    }
}
