package edu.kmaooad.handler.impl;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.handler.GlobalRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;

@Component
public class CancelHandler implements GlobalRequestHandler {

    private final StartCommandHandler startCommandHandler;

    public CancelHandler(StartCommandHandler startCommandHandler) {
        this.startCommandHandler = startCommandHandler;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate(), GlobalConstants.CANCEL_BUTTON_LABEL);
    }

    @Override
    public HandlerResponse handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        userSession.setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        userSession.setDialogState(null);

        return startCommandHandler.handle(userRequest);
    }
}
