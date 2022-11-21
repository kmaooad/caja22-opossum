package edu.kmaooad.handler.impl.group;

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

import java.util.List;

@Component
public class GroupButtonsHandler extends UserRequestHandler {

    public static String GROUPS_MAIN = "Групи";
    public static List<String> buttons = List.of("Редагувати", "Видалити", "Додати");

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public GroupButtonsHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), GROUPS_MAIN) ||
                ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE.equals(userRequest.getUserSession().getState());
    }

    @Override
    public BotApiMethod<?> handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildAdditionalActions(buttons);
        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        return telegramService.sendMessage(userRequest.getChatId(), "Оберіть що хочете робити⤵️", replyKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}
