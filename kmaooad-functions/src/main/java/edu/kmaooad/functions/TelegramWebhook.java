package edu.kmaooad.functions;

import edu.kmaooad.DTO.BotUpdateResult;
import edu.kmaooad.service.TelegramMessagesService;
import edu.kmaooad.telegram.StudentsBot;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private TelegramMessagesService messagesService;
    private StudentsBot studentsBot;

    @Autowired
    public TelegramWebhook(TelegramMessagesService messagesService,
                           StudentsBot studentsBot) {
        this.messagesService = messagesService;
        this.studentsBot = studentsBot;
    }

    public BotApiMethod<?> apply(Update upd) {
        try {
//            messagesService.addMessage(modelMapper.map(upd, TelegramMessage.class));
            log.error(upd.toString());
            return studentsBot.onWebhookUpdateReceived(upd);
//            return BotUpdateResult.Ok("1", 3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}