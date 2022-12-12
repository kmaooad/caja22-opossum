package edu.kmaooad.handler.impl.template.button;

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
public class TemplateButtonsHandler implements ButtonRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public TemplateButtonsHandler(TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return UserRequestHandler.isTextMessage(request.getUpdate(), GlobalConstants.GROUP_TEMPLATES_BUTTON_LABEL) &&
                request.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest request) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildGroupTemplateMenuWithCancel();
        request.getUserSession().setConversationState(ConversationState.WAITING_FOR_TEMPLATE_ACTION_CHOICE);

        return new HandlerResponse(telegramService.sendMessage(request.getChatId(), "Оберіть що хочете робити⤵️", replyKeyboardMarkup), true);
    }
}
