package edu.kmaooad;

import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.DialogHandler;
import edu.kmaooad.handler.GlobalRequestHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Handler;

@Component
@Slf4j
public class Dispatcher {

    private final List<GlobalRequestHandler> globalRequestHandlers;
    private final List<DialogHandler> dialogHandlers;
    private final List<ButtonRequestHandler> buttonRequestHandlers;

    public Dispatcher(List<GlobalRequestHandler> globalRequestHandlers, List<DialogHandler> dialogHandlers, List<ButtonRequestHandler> buttonRequestHandlers) {
        this.globalRequestHandlers = globalRequestHandlers;
        this.dialogHandlers = dialogHandlers;
        this.buttonRequestHandlers = buttonRequestHandlers;
    }

    public HandlerResponse dispatch(UserRequest userRequest) {
        log.info("Input session: " + userRequest.getUserSession());

        for (GlobalRequestHandler handler : globalRequestHandlers) {
            if (handler.isApplicable(userRequest)) {
                log.info("Output session: " + userRequest.getUserSession());
                return handler.handle(userRequest);
            }
        }

        for (ButtonRequestHandler handler : buttonRequestHandlers) {
            if (handler.isApplicable(userRequest)) {
                log.info("Output session: " + userRequest.getUserSession());
                return handler.handle(userRequest);
            }
        }

        if (userRequest.getUserSession().getDialogState() != null) {
            for (DialogHandler handler : dialogHandlers) {
                if (handler.isApplicable(userRequest)) {
                    log.info("Output session: " + userRequest.getUserSession());
                    return handler.handle(userRequest);
                }
            }
        }

        return new HandlerResponse(null, false);
    }
}

