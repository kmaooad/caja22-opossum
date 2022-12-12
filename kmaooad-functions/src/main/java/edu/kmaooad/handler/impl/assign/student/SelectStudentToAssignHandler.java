package edu.kmaooad.handler.impl.assign.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.StudentService;
import edu.kmaooad.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SelectStudentToAssignHandler implements ButtonRequestHandler {

    private final KeyboardHelper keyboardHelper;
    private final StudentService studentService;
    private final TelegramService telegramService;

    SelectStudentToAssignHandler(KeyboardHelper keyboardHelper, StudentService groupService,
                                 TelegramService telegramService) {
        this.keyboardHelper = keyboardHelper;
        this.studentService = groupService;
        this.telegramService = telegramService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate(), GlobalConstants.STUDENT_SHOW_ALL_ASSIGN_LABEL) &&
                userRequest.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest userRequest) {
        List<String> studentNames = studentService.getAllStudents().stream()
                .map(s -> s.getFirstName() + ' ' + s.getLastName() + ' ' + s.getPatronym() + '\n' + s.getEmail())
                .collect(Collectors.toList());

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildAdditionalActionsVertical(studentNames);
        userRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_STUDENT_TO_ASSIGN_CHOICE);

        return new HandlerResponse(telegramService.sendMessage(userRequest.getChatId(), "Оберіть студента якому хочете додати активності⤵️", replyKeyboardMarkup), true);

    }
}
