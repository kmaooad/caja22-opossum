package edu.kmaooad.handler.impl.template.button;

import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateTemplateButtonHandler implements ButtonRequestHandler {
    @Override
    public boolean isApplicable(UserRequest request) {
        return false;
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        return null;
    }
}
