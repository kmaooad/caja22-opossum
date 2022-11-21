package edu.kmaooad.handler.impl.group.create;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class GetGroupNameHandler extends UserRequestHandler {
    private final GroupService groupService;

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;

    public GetGroupNameHandler(GroupService groupService, TelegramService telegramService, UserSessionService userSessionService) {
        this.groupService = groupService;
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate()) &&
                ConversationState.WAITING_FOR_GROUP_NAME.equals(userRequest.getUserSession().getState());
    }

    @Override
    public BotApiMethod<?> handle(UserRequest dispatchRequest) {
        UserSession userSession = dispatchRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        //Add group to db
        String name = dispatchRequest.getUpdate().getMessage().getText();
        Group group = new Group();
        group.setName(name);
        boolean added = groupService.addGroup(group);

        if (added) {
            return telegramService.sendMessage(dispatchRequest.getChatId(), "Успішно додано групу: " + name + '.');
        } else {
            return telegramService.sendMessage(dispatchRequest.getChatId(), "Не змогли додати групу з ім'ям: " + name + '.');
        }
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
