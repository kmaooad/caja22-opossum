package edu.kmaooad.handler.impl;

import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.telegram.StudentsBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
public class StartCommandHandler extends UserRequestHandler {

    private static String command = "/start";

    private final StudentsBot studentsBot;
    private final KeyboardHelper keyboardHelper;

    public StartCommandHandler( StudentsBot studentsBot, KeyboardHelper keyboardHelper) {
        this.studentsBot = studentsBot;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public BotApiMethod<?> handle(UserRequest request) {
        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        studentsBot.sendMessage(request.getChatId(),
                "\uD83D\uDC4BПривіт! Вітаю у чаті команди опосумів",
                replyKeyboard);

        return studentsBot.sendMessage(request.getChatId(),
                "Обирайте з меню нижче ⤵️");
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
