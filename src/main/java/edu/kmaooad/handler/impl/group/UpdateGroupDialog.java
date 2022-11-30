
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
public class UpdateGroupDialog extends DialogHandler {
    private final UserSessionService userSessionService;
    private final GroupService groupService;
    private final TelegramService telegramService;
    private final GroupButtonsHandler groupButtonsHandler;

    UpdateGroupDialog(AskGroupIDHandler askGroupIDHandler, GetGroupIDHandler getGroupIDHandler,
                      AskGroupNameHandler askGroupNameHandler, GetGroupNameHandler getGroupNameHandler,
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

        PostActions getGroupById = (request, response) -> {
            if (response.isSuccess()) {
                Group groupWithID = (Group) request.getUserSession().getData().get(GroupConstants.GROUP_MAP_KEY);
                Group groupDB = groupService.getGroupById(groupWithID.getId());

                if (groupDB == null) {
                    log.warn("Group not found: " + groupWithID.getId());
                    //State will automatically switch if group was correct. To ask again we should move back.
                    request.getUserSession().setConversationState(ConversationState.WAITING_FOR_GROUP_ID);
                    return new HandlerResponse(telegramService.sendMessage(request.getChatId(), String.format(GroupConstants.NO_GROUP_WITH_ID, groupWithID.getId())), false);
                } else {
                    telegramService.sendMessage(request.getChatId(), GroupConstants.groupToString(groupDB));
                    request.getUserSession().getData().replace(GroupConstants.GROUP_MAP_KEY, groupDB);
                    //Ask changes
                    return handle(request);
                }
            }
            return response;
        };

        handlers.put(ConversationState.ASK_FOR_GROUP_ID, new DialogSingleHandler(askGroupIDHandler));
        handlers.put(ConversationState.WAITING_FOR_GROUP_ID, new DialogSingleHandler(getGroupIDHandler, getGroupById));

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
        log.info("Some response: " + userRequest.getUserSession().getData().keySet().toString());
        return super.startDialog(userRequest);
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return request.getUserSession().getDialogState().equals(getDialogType());
    }

    @Override
    protected void finishActions(UserRequest dispatchRequest) {
        log.warn("AddGroupDialog finish actions: " + dispatchRequest.getUserSession().getData().get("group"));
        Group group = (Group) dispatchRequest.getUserSession().getData().get(GroupConstants.GROUP_MAP_KEY);
        if (groupService.updateGroup(group)) {
            log.info("Successfully update group: " + group);
            telegramService.sendMessage(dispatchRequest.getChatId(), String.format(GroupConstants.SUCCESSFULLY_UPDATED, GroupConstants.groupToString(group)));
        } else {
            log.error("Cannot update group: " + group);
            telegramService.sendMessage(dispatchRequest.getChatId(), GroupConstants.ERROR_WHILE_UPDATE);
        }
        dispatchRequest.getUserSession().setDialogState(null);
        //Go back to the group menu
        groupButtonsHandler.handle(dispatchRequest);
    }

    @Override
    public DialogState getDialogType() {
        return DialogState.UPDATE_GROUP;
    }
}
