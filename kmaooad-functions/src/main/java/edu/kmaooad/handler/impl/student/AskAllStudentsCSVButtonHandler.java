package edu.kmaooad.handler.impl.student;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.StudentConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static edu.kmaooad.constants.bot.StudentConstants.STUDENT_CSV_FORMAT_EXAMPLE;

@Component
@Slf4j
public class AskAllStudentsCSVButtonHandler implements ButtonRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public AskAllStudentsCSVButtonHandler(KeyboardHelper keyboardHelper, TelegramService telegramService, UserSessionService userSessionService, StudentButtonsHandler studentButtonsHandler) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return UserRequestHandler.isTextMessage(request.getUpdate(), StudentConstants.STUDENT_UPDATE_CSV_BUTTON_LABEL) &&
                request.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_STUDENT_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        log.info("Asking for student CSV");
        //Set previous state for studentButtonsHandler
        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_STUDENT_CSV);
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        return new HandlerResponse(telegramService.sendMessage(dispatchRequest.getChatId(), STUDENT_CSV_FORMAT_EXAMPLE, replyKeyboardMarkup), true);
    }
}