package edu.kmaooad.handler.impl.template.button;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.handler.impl.template.dialog.UpdateTemplateDialog;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateTemplateButtonHandler implements ButtonRequestHandler {
    private final UpdateTemplateDialog updateTemplateDialog;

    public UpdateTemplateButtonHandler(UpdateTemplateDialog updateTemplateDialog) {
        this.updateTemplateDialog = updateTemplateDialog;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return UserRequestHandler.isTextMessage(request.getUpdate(), GroupTemplateConstants.GROUP_TEMPLATE_EDIT_BUTTON_LABEL) &&
                request.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_TEMPLATE_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        return updateTemplateDialog.startDialog(dispatchRequest);
    }
}
