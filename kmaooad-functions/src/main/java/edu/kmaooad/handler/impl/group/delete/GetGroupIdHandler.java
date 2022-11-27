package edu.kmaooad.handler.impl.group.delete;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.handler.impl.group.GroupButtonsHandler;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Component
public class GetGroupIdHandler extends UserRequestHandler {
    private final GroupService groupService;

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;
    private final GroupButtonsHandler groupButtonsHandler;

    public GetGroupIdHandler(GroupService groupService, TelegramService telegramService, UserSessionService userSessionService, GroupButtonsHandler groupButtonsHandler) {
        this.groupService = groupService;
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
        this.groupButtonsHandler = groupButtonsHandler;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate()) &&
                ConversationState.WAITING_FOR_GROUP_ID.equals(userRequest.getUserSession().getConversationState());
    }

    @Override
    public BotApiMethod<?> handle(UserRequest dispatchRequest) {
        UserSession userSession = dispatchRequest.getUserSession();
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        //Add group to db
        String groupId = dispatchRequest.getUpdate().getMessage().getText();
        boolean added = groupService.deleteGroup(groupId);

        BotApiMethod<?> result;
        if (added) {
            result = telegramService.sendMessage(dispatchRequest.getChatId(), "Успішно видалено групу: <b>" + groupId + "</b>.");
        } else {
            result = telegramService.sendMessage(dispatchRequest.getChatId(), "Не змогли видалити групу з ім'ям: <b>" + groupId + "</b>.");
        }
        groupButtonsHandler.handle(dispatchRequest);
        return result;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
