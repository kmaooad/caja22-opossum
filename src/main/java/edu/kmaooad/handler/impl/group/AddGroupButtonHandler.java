package edu.kmaooad.handler.impl.group;

import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddGroupButtonHandler implements ButtonRequestHandler {
    private final AddGroupDialog addGroupDialog;

    public AddGroupButtonHandler(AddGroupDialog addGroupDialog) {
        this.addGroupDialog = addGroupDialog;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate(), GroupConstants.GROUP_ADD_BUTTON_LABEL);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        log.info("Add group dialog started");
        return addGroupDialog.startDialog(dispatchRequest);
    }
}
