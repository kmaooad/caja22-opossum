package edu.kmaooad.handler.impl;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.handler.GlobalRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
public class StartCommandHandler implements GlobalRequestHandler{

    private static String command = "/start";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public StartCommandHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public HandlerResponse handle(UserRequest request) {
        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        request.getUserSession().setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        userSessionService.saveSession(request.getChatId(), request.getUserSession());
        telegramService.sendMessage(request.getChatId(),
                "\uD83D\uDC4BПривіт! Вітаю у чаті команди опосумів",
                replyKeyboard);

        return new HandlerResponse(telegramService.sendMessage(request.getChatId(),
                "Обирайте з меню нижче ⤵️"), true);
    }
}
