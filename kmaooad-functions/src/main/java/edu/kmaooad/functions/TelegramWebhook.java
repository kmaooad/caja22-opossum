package edu.kmaooad.functions;

import edu.kmaooad.DTO.BotUpdate;
import edu.kmaooad.DTO.BotUpdateResult;
import edu.kmaooad.model.TelegramMessage;
import edu.kmaooad.service.TelegramMessagesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TelegramWebhook implements Function<BotUpdate, BotUpdateResult> {

    @Autowired
    private ModelMapper modelMapper;
    private TelegramMessagesService messagesService;

    @Autowired
    public TelegramWebhook(TelegramMessagesService messagesService){
        this.messagesService = messagesService;
    }
    public BotUpdateResult apply(BotUpdate upd) {
        try {
            messagesService.addMessage(modelMapper.map(upd, TelegramMessage.class));
            return BotUpdateResult.Ok(upd.getMessageId(), 3);
        } catch (Exception e){
            e.printStackTrace();
            return BotUpdateResult.Nok(upd.getMessageId());
        }
    }
}