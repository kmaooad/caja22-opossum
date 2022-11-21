package edu.kmaooad.handler.impl.group.read;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.handler.impl.group.GroupTemplateAnswers;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.List;

@Component
@Slf4j
public class GetAllGroupsHandler extends UserRequestHandler {
    private final GroupService groupService;

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public static String ACTION = "Показати всі групи";

    public GetAllGroupsHandler(GroupService groupService, TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.groupService = groupService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return isTextMessage(request.getUpdate(), ACTION);
    }

    @Override
    public BotApiMethod<?> handle(UserRequest dispatchRequest) {
        UserSession userSession = dispatchRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        List<Group> groups = groupService.getAllGroups();
        log.info("Groups: " + groups.toString());

        for (Group group : groups) {
            telegramService.sendMessage(dispatchRequest.getChatId(),
                    String.format(GroupTemplateAnswers.SHOW_FULL_GROUP, group.getName(), group.getId()));
        }

        return null;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
