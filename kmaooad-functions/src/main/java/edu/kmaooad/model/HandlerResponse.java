package edu.kmaooad.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Data
@AllArgsConstructor
public class HandlerResponse {
    private BotApiMethod<?> result;
    private boolean success;
}
