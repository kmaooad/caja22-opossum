package edu.kmaooad.handler.impl.template.button;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.handler.impl.template.dialog.DeleteTemplateDialog;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeleteTemplateButtonHandler implements ButtonRequestHandler {
    private final DeleteTemplateDialog deleteTemplateDialog;

    public DeleteTemplateButtonHandler(DeleteTemplateDialog deleteTemplateDialog) {
        this.deleteTemplateDialog = deleteTemplateDialog;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return UserRequestHandler.isTextMessage(request.getUpdate(), GroupTemplateConstants.GROUP_TEMPLATE_DELETE_BUTTON_LABEL) &&
                request.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_TEMPLATE_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        log.info("Delete group dialog started, chatId: " + dispatchRequest.getChatId());
        return deleteTemplateDialog.startDialog(dispatchRequest);
    }
}
