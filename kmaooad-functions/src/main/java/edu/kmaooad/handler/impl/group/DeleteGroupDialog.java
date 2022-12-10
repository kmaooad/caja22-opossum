
package edu.kmaooad.handler.impl.group;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.DialogHandler;
import edu.kmaooad.handler.impl.group.common.AskGroupIDHandler;
import edu.kmaooad.handler.impl.group.common.GetGroupIDHandler;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.ServiceException;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@Slf4j
public class DeleteGroupDialog extends DialogHandler {
    private final UserSessionService userSessionService;
    private final GroupService groupService;
    private final TelegramService telegramService;
    private final GroupButtonsHandler groupButtonsHandler;

    DeleteGroupDialog(AskGroupIDHandler askGroupIDHandler, GetGroupIDHandler getGroupIDHandler,
                      GroupButtonsHandler groupButtonsHandler,
                      UserSessionService userSessionService, GroupService groupService, TelegramService telegramService) {
        this.userSessionService = userSessionService;
        this.groupService = groupService;
        this.telegramService = telegramService;
        this.groupButtonsHandler = groupButtonsHandler;

        handlers = new LinkedHashMap<>();

        handlers.put(ConversationState.ASK_FOR_GROUP_ID, new DialogSingleHandler(askGroupIDHandler));
        handlers.put(ConversationState.WAITING_FOR_GROUP_ID, new DialogSingleHandler(getGroupIDHandler));
    }

    @Override
    public final HandlerResponse startDialog(UserRequest userRequest) {
        userRequest.getUserSession().getData().put(GroupConstants.GROUP_MAP_KEY, new Group());
        userSessionService.saveSession(userRequest.getChatId(), userRequest.getUserSession());
        log.info("Some response: " + userRequest.getUserSession().getData().keySet());
        return super.startDialog(userRequest);
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return request.getUserSession().getDialogState().equals(getDialogType());
    }

    @Override
    protected void finishActions(UserRequest dispatchRequest) {
        Group group = (Group) dispatchRequest.getUserSession().getData().get(GroupConstants.GROUP_MAP_KEY);
        try {
            groupService.deleteGroup(group.getId());
            log.info("Successfully deleted group: " + group);
            telegramService.sendMessage(dispatchRequest.getChatId(), String.format(GroupConstants.SUCCESSFULLY_DELETED, GroupConstants.groupToString(group)));
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("Cannot delete group: " + group);
            telegramService.sendMessage(dispatchRequest.getChatId(), GroupConstants.ERROR_WHILE_DELETE);
        }
        dispatchRequest.getUserSession().setDialogState(null);
        //Go back to the group menu
        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        groupButtonsHandler.handle(dispatchRequest);
    }

    @Override
    public DialogState getDialogType() {
        return DialogState.DELETE_GROUP;
    }
}
