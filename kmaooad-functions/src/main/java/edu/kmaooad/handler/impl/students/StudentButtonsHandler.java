package edu.kmaooad.handler.impl.students;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
public class StudentButtonsHandler extends UserRequestHandler {

    public static String STUDENTS_MAIN = "Студенти";
    public static List<String> cities = List.of("Додати студентів з csv", "Оновити студентів з csv");

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public StudentButtonsHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), STUDENTS_MAIN);
    }

    @Override
    public BotApiMethod<?> handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildAdditionalActions(cities);
        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_CHOICE);
        userSessionService.saveSession(userSession.getChatId(), userSession);

        return telegramService.sendMessage(userRequest.getChatId(),"Оберіть що хочете робити⤵️", replyKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

}
