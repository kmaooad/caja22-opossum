package edu.kmaooad.functions;

import edu.kmaooad.telegram.StudentsBot;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Function;

@Slf4j
@Component
public class TelegramWebhook implements Function<Update, BotApiMethod<?>> {

    @Autowired
    private ModelMapper modelMapper;

    private final StudentsBot studentsBot;


    @Autowired
    public TelegramWebhook(StudentsBot studentsBot) {
        this.studentsBot = studentsBot;
    }

    public BotApiMethod<?> apply(Update upd) {
        try {
            log.error(upd.toString());
            return studentsBot.onWebhookUpdateReceived(upd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
