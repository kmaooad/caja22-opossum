package edu.kmaooad.handler.impl.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class StudentButtonsHandler implements ButtonRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public StudentButtonsHandler(TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate(), GlobalConstants.STUDENTS_BUTTON_LABEL) &&
                userRequest.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildStudentMenuWithCancel();
        userRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_STUDENT_ACTION_CHOICE);

        return new HandlerResponse(telegramService.sendMessage(userRequest.getChatId(), "Оберіть що хочете робити⤵️", replyKeyboardMarkup), true);
    }
}
