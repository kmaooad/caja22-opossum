package edu.kmaooad;

import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.DialogHandler;
import edu.kmaooad.handler.GlobalRequestHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Dispatcher {

    private final List<GlobalRequestHandler> globalRequestHandlers;
    private final List<DialogHandler> dialogHandlers;
    private final List<ButtonRequestHandler> buttonRequestHandlers;
    private final UserSessionService userSessionService;

    public Dispatcher(List<GlobalRequestHandler> globalRequestHandlers, List<DialogHandler> dialogHandlers, List<ButtonRequestHandler> buttonRequestHandlers, UserSessionService userSessionService) {
        this.globalRequestHandlers = globalRequestHandlers;
        this.dialogHandlers = dialogHandlers;
        this.buttonRequestHandlers = buttonRequestHandlers;
        this.userSessionService = userSessionService;
    }

    public HandlerResponse dispatch(UserRequest userRequest) {

        for (GlobalRequestHandler handler : globalRequestHandlers) {
            if (handler.isApplicable(userRequest)) {
                return handler.handle(userRequest);
            }
        }

        for (ButtonRequestHandler handler : buttonRequestHandlers) {
            if (handler.isApplicable(userRequest)) {
                return handler.handle(userRequest);
            }
        }

        if (userRequest.getUserSession().getDialogState() != null) {
            for (DialogHandler handler : dialogHandlers) {
                if (handler.isApplicable(userRequest)) {
                    return handler.handle(userRequest);
                }
            }
        }

        return null;
    }
}

