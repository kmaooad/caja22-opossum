package edu.kmaooad.handler.impl.group;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateGroupButtonHandler implements ButtonRequestHandler {
    private final UpdateGroupDialog updateGroupDialog;

    public UpdateGroupButtonHandler(UpdateGroupDialog updateGroupDialog) {
        this.updateGroupDialog = updateGroupDialog;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate(), GroupConstants.GROUP_EDIT_BUTTON_LABEL) &&
                userRequest.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        log.info("Update group dialog started, chatId: " + dispatchRequest.getChatId());
        return updateGroupDialog.startDialog(dispatchRequest);
    }
}
