package edu.kmaooad.handler.impl.group;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.DialogHandler;
import edu.kmaooad.handler.impl.group.common.*;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@Slf4j
public class AddGroupDialog extends DialogHandler {
    private final UserSessionService userSessionService;
    private final GroupService groupService;
    private final TelegramService telegramService;
    private final GroupButtonsHandler groupButtonsHandler;

    AddGroupDialog(AskGroupNameHandler askGroupNameHandler, GetGroupNameHandler getGroupNameHandler,
                   AskGroupGradeHandler askGroupGradeHandler, GetGroupGradeHandler getGroupGradeHandler,
                   AskGroupYearHandler askGroupYearHandler, GetGroupYearHandler getGroupYearHandler,
                   GroupButtonsHandler groupButtonsHandler,
                   UserSessionService userSessionService, GroupService groupService, TelegramService telegramService) {
        this.userSessionService = userSessionService;
        this.groupService = groupService;
        this.telegramService = telegramService;
        this.groupButtonsHandler = groupButtonsHandler;

        handlers = new LinkedHashMap<>();

        PostActions moveToNext = (request, response) -> {
            if (response.isSuccess()) {
                return handle(request);
            }
            return response;
        };

        handlers.put(ConversationState.ASK_FOR_GROUP_NAME, new DialogSingleHandler(askGroupNameHandler));
        handlers.put(ConversationState.WAITING_FOR_GROUP_NAME, new DialogSingleHandler(getGroupNameHandler, moveToNext));

        handlers.put(ConversationState.ASK_FOR_GROUP_GRADE, new DialogSingleHandler(askGroupGradeHandler));
        handlers.put(ConversationState.WAITING_FOR_GROUP_GRADE, new DialogSingleHandler(getGroupGradeHandler, moveToNext));

        handlers.put(ConversationState.ASK_FOR_GROUP_YEAR, new DialogSingleHandler(askGroupYearHandler));
        handlers.put(ConversationState.WAITING_FOR_GROUP_YEAR, new DialogSingleHandler(getGroupYearHandler));
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
    //todo
    // take off comment
    @Override
    protected void finishActions(UserRequest dispatchRequest) {
        log.warn("AddGroupDialog finish actions: " + dispatchRequest.getUserSession().getData().get("group"));
        Group group = (Group) dispatchRequest.getUserSession().getData().get(GroupConstants.GROUP_MAP_KEY);
      /*  if (groupService.addGroup(group)) {
            log.info("Successfully added group: " + group);
            telegramService.sendMessage(dispatchRequest.getChatId(), GroupConstants.SUCCESSFULLY_ADDED);
        } else {
            log.error("Cannot add group: " + group);
            telegramService.sendMessage(dispatchRequest.getChatId(), GroupConstants.ERROR_WHILE_ADD);
        }*/
        dispatchRequest.getUserSession().setDialogState(null);
        //Go back to the group menu
        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        groupButtonsHandler.handle(dispatchRequest);
    }

    @Override
    public DialogState getDialogType() {
        return DialogState.ADD_GROUP;
    }
}
