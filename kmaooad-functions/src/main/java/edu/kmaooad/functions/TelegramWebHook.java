package edu.kmaooad.functions;

import edu.kmaooad.DTO.BotUpdate;
import edu.kmaooad.DTO.BotUpdateResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class TelegramWebHook implements Function<Mono<BotUpdate>, Mono<BotUpdateResult>> {


    public Mono<BotUpdateResult> apply(Mono<BotUpdate> mono) {
        return mono.map(upd -> BotUpdateResult.Ok(upd.getMessageId(), 1));
    }
}
