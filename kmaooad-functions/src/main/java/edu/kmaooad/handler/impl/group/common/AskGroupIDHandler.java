package edu.kmaooad.handler.impl.group.common;

import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.TelegramService;
import org.springframework.stereotype.Component;

@Component
public class AskGroupIDHandler extends CommonRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public AskGroupIDHandler(TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        return new HandlerResponse(telegramService.sendMessage(dispatchRequest.getChatId(), GroupConstants.ASK_FOR_GROUP_ID, keyboardHelper.buildMenuWithCancel()), true);
    }
}
