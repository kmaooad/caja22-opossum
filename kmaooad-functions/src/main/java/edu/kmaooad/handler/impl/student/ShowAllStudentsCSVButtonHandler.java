package edu.kmaooad.handler.impl.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.StudentConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.Student;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.MassStudentsService;
import edu.kmaooad.service.StudentService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ShowAllStudentsCSVButtonHandler implements ButtonRequestHandler {
    private final StudentService studentService;

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;
    private final StudentButtonsHandler studentButtonsHandler;
    private final MassStudentsService massStudentsService;

    public ShowAllStudentsCSVButtonHandler(StudentService studentService, MassStudentsService massStudentsService, TelegramService telegramService, UserSessionService userSessionService, StudentButtonsHandler studentButtonsHandler) {
        this.studentService = studentService;
        this.massStudentsService = massStudentsService;
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
        this.studentButtonsHandler = studentButtonsHandler;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return UserRequestHandler.isTextMessage(request.getUpdate(), StudentConstants.STUDENT_SHOW_CSV_BUTTON_LABEL) &&
                request.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_STUDENT_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        List<Student> students = studentService.getAllStudents();
        log.info("Students csv: " + students.toString());
        String message = massStudentsService.generateStudentCSV(studentService.getAllStudents());
        telegramService.sendMessage(dispatchRequest.getChatId(), message);
        //Set previous state for studentButtonsHandler
        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        return studentButtonsHandler.handle(dispatchRequest);
    }
}