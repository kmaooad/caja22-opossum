package edu.kmaooad.handler.impl.group.create;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.handler.impl.group.GroupConstants;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class AddGroupButtonHandler extends UserRequestHandler {
    public static final String ACTION = "Додати";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public AddGroupButtonHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
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
        BotApiMethod<?> result = telegramService.sendMessage(dispatchRequest.getChatId(), GroupConstants.ASK_FOR_GROUP_NAME, replyKeyboardMarkup);

        UserSession userSession = dispatchRequest.getUserSession();

        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_NAME);
        userSession.setDialogState(DialogState.ADD_GROUP);

        Group group = new Group();
        userSession.getData().put(GroupConstants.GROUP_MAP_KEY, group);

        userSessionService.saveSession(userSession.getChatId(), userSession);

        return result;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
