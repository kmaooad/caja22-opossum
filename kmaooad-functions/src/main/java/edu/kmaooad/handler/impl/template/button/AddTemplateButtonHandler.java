package edu.kmaooad.handler.impl.template.button;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.handler.impl.template.dialog.AddTemplateDialog;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddTemplateButtonHandler implements ButtonRequestHandler {
    private final AddTemplateDialog addTemplateDialog;

    public AddTemplateButtonHandler(AddTemplateDialog addTemplateDialog) {
        this.addTemplateDialog = addTemplateDialog;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate(), GroupTemplateConstants.GROUP_TEMPLATE_ADD_BUTTON_LABEL) &&
                userRequest.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_TEMPLATE_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        log.info("Add group dialog started");
        return addTemplateDialog.startDialog(dispatchRequest);
    }
}
