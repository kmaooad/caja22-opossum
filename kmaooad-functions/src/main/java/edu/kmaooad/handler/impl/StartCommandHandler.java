package edu.kmaooad.handler.impl;

import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
public class StartCommandHandler extends UserRequestHandler {

    private static String command = "/start";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public StartCommandHandler( TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public BotApiMethod<?> handle(UserRequest request) {
        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        telegramService.sendMessage(request.getChatId(),
                "\uD83D\uDC4BПривіт! Вітаю у чаті команди опосумів",
                replyKeyboard);

        return telegramService.sendMessage(request.getChatId(),
                "Обирайте з меню нижче ⤵️");
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
