package edu.kmaooad.handler.impl.students.massAdd;

import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.MassStudentsService;
import edu.kmaooad.service.StudentService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

/**
 * This handler gets file from telegram, parse it and call student service add methods
 */

@Component
public class GetFileHandler extends UserRequestHandler {

    private MassStudentsService studentsService;

    public GetFileHandler(MassStudentsService studentsService) {
        this.studentsService = studentsService;
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
