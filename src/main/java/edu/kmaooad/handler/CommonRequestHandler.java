package edu.kmaooad.handler;

import edu.kmaooad.model.UserRequest;

public abstract class CommonRequestHandler implements UserRequestHandler{
    @Override
    public final boolean isApplicable(UserRequest request) {
        return true;
    }
}
