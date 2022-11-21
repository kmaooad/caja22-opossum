package edu.kmaooad.handler.impl.group.create;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class AddGroupHandler extends UserRequestHandler {
    public static final String ACTION = "Додати";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public AddGroupHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), ACTION);
    }

    @Override
    public BotApiMethod<?> handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        BotApiMethod<?> result = telegramService.sendMessage(dispatchRequest.getChatId(), "Вкажіть назву групи.", replyKeyboardMarkup);

        UserSession userSession = dispatchRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_GROUP_NAME);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        return result;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}