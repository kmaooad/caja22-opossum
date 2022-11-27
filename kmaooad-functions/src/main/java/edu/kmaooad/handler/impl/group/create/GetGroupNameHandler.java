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

import java.util.Arrays;

@Component
public class GetGroupNameHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public GetGroupNameHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate()) &&
                DialogState.ADD_GROUP.equals(userRequest.getUserSession().getDialogState()) &&
                ConversationState.WAITING_FOR_GROUP_NAME.equals(userRequest.getUserSession().getConversationState());
    }

    @Override
    public BotApiMethod<?> handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();

        UserSession userSession = dispatchRequest.getUserSession();

        System.out.println(Arrays.toString(userSession.getData().keySet().toArray()));
        Group group = (Group) userSession.getData().get(GroupConstants.GROUP_MAP_KEY);
        group.setName(dispatchRequest.getUpdate().getMessage().getText());

        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_GRADE);

        userSessionService.saveSession(userSession.getChatId(), userSession);


        return telegramService.sendMessage(dispatchRequest.getChatId(), GroupConstants.ASK_FOR_GROUP_GRADE, replyKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
