package edu.kmaooad.handler.impl.group;

import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeleteGroupButtonHandler implements ButtonRequestHandler {
    private final DeleteGroupDialog deleteGroupDialog;

    public DeleteGroupButtonHandler(DeleteGroupDialog deleteGroupDialog) {
        this.deleteGroupDialog = deleteGroupDialog;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate(), GroupConstants.GROUP_DELETE_BUTTON_LABEL);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        log.info("Delete group dialog started, chatId: " + dispatchRequest.getChatId());
        return deleteGroupDialog.startDialog(dispatchRequest);
    }
}
