package edu.kmaooad.handler;

import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserRequestHandler {

    boolean isApplicable(UserRequest request);
    HandlerResponse handle(UserRequest dispatchRequest);

    static boolean isCommand(Update update, String command) {
        return update.hasMessage() && update.getMessage().isCommand()
                && update.getMessage().getText().equals(command);
    }

    static boolean isTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    static boolean isTextMessage(Update update, String text) {
        return isTextMessage(update) && update.getMessage().getText().equals(text);
    }
}