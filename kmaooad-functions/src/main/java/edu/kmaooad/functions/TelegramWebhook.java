package edu.kmaooad.functions;

import edu.kmaooad.DTO.BotUpdate;
import edu.kmaooad.DTO.BotUpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TelegramWebhook implements Function<BotUpdate, BotUpdateResult> {

    @Autowired
    public TelegramWebhook(){

    }
    public BotUpdateResult apply(BotUpdate upd) {
        return BotUpdateResult.Ok(upd.getMessageId(), 3);
    }
}