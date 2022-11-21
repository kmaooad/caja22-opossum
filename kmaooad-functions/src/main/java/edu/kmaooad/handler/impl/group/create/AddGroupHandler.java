package edu.kmaooad.handler.impl.group.create;

import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.GroupService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public class AddGroupHandler extends UserRequestHandler {
    private final GroupService groupService;

    public AddGroupHandler(GroupService groupService) {
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
