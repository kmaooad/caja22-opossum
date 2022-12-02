package edu.kmaooad.handler.impl.group;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ShowAllGroupsButtonHandler implements ButtonRequestHandler {
    private final GroupService groupService;

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;
    private final GroupButtonsHandler groupButtonsHandler;

    public ShowAllGroupsButtonHandler(GroupService groupService, TelegramService telegramService, UserSessionService userSessionService, GroupButtonsHandler groupButtonsHandler) {
        this.groupService = groupService;
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
        this.groupButtonsHandler = groupButtonsHandler;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return UserRequestHandler.isTextMessage(request.getUpdate(), GroupConstants.GROUP_SHOW_ALL_BUTTON_LABEL) &&
                request.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        List<Group> groups = groupService.getAllGroups();
        log.info("Groups: " + groups.toString());

        for (Group group : groups) {
            telegramService.sendMessage(dispatchRequest.getChatId(),
                    String.format(GroupConstants.SHOW_FULL_GROUP, group.getName(), group.getId(), group.getGrade(), group.getYear()));
        }

        //Set previous state for groupButtonsHandler
        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        return groupButtonsHandler.handle(dispatchRequest);
    }
}