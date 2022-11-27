package edu.kmaooad.handler.impl.group.delete;

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
public class DeleteGroupHandler extends UserRequestHandler {
    public static final String ACTION = "Видалити";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public DeleteGroupHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
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
        BotApiMethod<?> result = telegramService.sendMessage(dispatchRequest.getChatId(), "Вкажіть ідентифікатор групи.", replyKeyboardMarkup);

        UserSession userSession = dispatchRequest.getUserSession();
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_ID);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        return result;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
