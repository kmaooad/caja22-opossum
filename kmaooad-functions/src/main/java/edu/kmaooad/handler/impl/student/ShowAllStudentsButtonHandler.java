package edu.kmaooad.handler.impl.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.StudentConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.Student;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.StudentService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ShowAllStudentsButtonHandler implements ButtonRequestHandler {
    private final StudentService studentService;

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;
    private final StudentButtonsHandler studentButtonsHandler;

    public ShowAllStudentsButtonHandler(StudentService studentService, TelegramService telegramService, UserSessionService userSessionService, StudentButtonsHandler studentButtonsHandler) {
        this.studentService = studentService;
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
        this.studentButtonsHandler = studentButtonsHandler;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return UserRequestHandler.isTextMessage(request.getUpdate(), StudentConstants.STUDENT_SHOW_ALL_BUTTON_LABEL) &&
                request.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_STUDENT_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        List<Student> students = studentService.getAllStudents();
        log.info("Students: " + students.toString());

        for (Student student : students) {
            telegramService.sendMessage(dispatchRequest.getChatId(),
                    String.format(StudentConstants.SHOW_FULL_STUDENT,
                            student.getLastName(), student.getFirstName(), student.getPatronym(),
                            student.getId(),
                            student.getEmail(), student.getDepartment(), student.getActivities().size()));
        }

        //Set previous state for studentButtonsHandler
        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_STUDENT_ACTION_CHOICE);
        return studentButtonsHandler.handle(dispatchRequest);
    }
}