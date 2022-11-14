package edu.kmaooad.handler.impl;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.UserSessionService;
import edu.kmaooad.telegram.StudentsBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
public class StudentMenuHandler extends UserRequestHandler {

    public static String STUDENTS_MAIN = "Студенти";
    public static List<String> cities = List.of("Київ", "Львів");

    private final StudentsBot studentsBot;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public StudentMenuHandler(StudentsBot studentsBot, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.studentsBot = studentsBot;
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

        return studentsBot.sendMessage(userRequest.getChatId(),"Оберіть що хочете робити⤵️", replyKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

}
