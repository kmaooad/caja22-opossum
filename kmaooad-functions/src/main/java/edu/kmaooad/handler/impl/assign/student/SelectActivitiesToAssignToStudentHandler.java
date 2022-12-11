package edu.kmaooad.handler.impl.assign.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.StudentConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.Student;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.ActivityService;
import edu.kmaooad.service.StudentService;
import edu.kmaooad.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@Slf4j
public class SelectActivitiesToAssignToStudentHandler implements ButtonRequestHandler {

    private final KeyboardHelper keyboardHelper;
    private final StudentService studentService;
    private final ActivityService activityService;
    private final TelegramService telegramService;

    SelectActivitiesToAssignToStudentHandler(KeyboardHelper keyboardHelper, TelegramService telegramService,
                                             StudentService studentService, ActivityService activityService
    ) {
        this.keyboardHelper = keyboardHelper;
        this.telegramService = telegramService;
        this.studentService = studentService;
        this.activityService = activityService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate()) &&
                userRequest.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_STUDENT_TO_ASSIGN_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest userRequest) {
        log.info(userRequest.toString());
        String text = userRequest.getUpdate().getMessage().getText();
        String[] textSplit = text.trim().split("\n");
        if (textSplit.length != 2) {
            log.warn("Cannot split " + text + "to find email");
            return new HandlerResponse(telegramService.sendMessage(userRequest.getChatId(), "Не існує такого студента"), true);
        }
        Student student = studentService.getStudentByEmail(textSplit[1]);
        log.info("User picked student with email " + textSplit[1] + ", found student " + student);
        userRequest.getUserSession().getData().put(StudentConstants.STUDENT_MAP_KEY, student);
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildAdditionalActionsVertical(activityService.getStatusOfActivitiesForStudent(student));
        userRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_STUDENT_ACTIVITY_ASSIGN_CHOICE);

        return new HandlerResponse(telegramService.sendMessage(userRequest.getChatId(), "Оберіть активність яку хочете додати до " + student.getFirstName() + " " + student.getLastName() + " ⤵️", replyKeyboardMarkup), true);
    }
}
