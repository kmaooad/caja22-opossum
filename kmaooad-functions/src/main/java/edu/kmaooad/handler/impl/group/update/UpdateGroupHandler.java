package edu.kmaooad.handler.impl.group.update;

import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.GroupService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Component
public class UpdateGroupHandler extends UserRequestHandler {
    private final GroupService groupService;

    public UpdateGroupHandler(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return false;
    }

    @Override
    public BotApiMethod<?> handle(UserRequest dispatchRequest) {
        return null;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
