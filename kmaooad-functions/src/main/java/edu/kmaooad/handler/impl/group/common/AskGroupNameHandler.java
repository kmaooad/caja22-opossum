package edu.kmaooad.handler.impl.group.common;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Arrays;

@Component
public class AskGroupNameHandler extends CommonRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public AskGroupNameHandler(TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        return new HandlerResponse(telegramService.sendMessage(dispatchRequest.getChatId(), GroupConstants.ASK_FOR_GROUP_NAME, keyboardHelper.buildMenuWithCancel()), true);
    }
}
