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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
public class CancelHandler extends UserRequestHandler {

    private final StudentsBot studentsBot;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public CancelHandler(StudentsBot studentsBot, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.studentsBot = studentsBot;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), "❌ Скасувати");
    }

    @Override
    public BotApiMethod<?> handle(UserRequest userRequest) {
        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        studentsBot.sendMessage(userRequest.getChatId(),
                "Обирайте з меню нижче ⤵️", replyKeyboard);

        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.CONVERSATION_STARTED);
        userSessionService.saveSession(userSession.getChatId(), userSession);
        return null;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
