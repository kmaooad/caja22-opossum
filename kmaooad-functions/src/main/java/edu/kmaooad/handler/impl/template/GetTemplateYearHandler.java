package edu.kmaooad.handler.impl.template;

import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.*;
import edu.kmaooad.service.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class GetTemplateYearHandler extends CommonRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public GetTemplateYearHandler(TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();

        String messageText = dispatchRequest.getUpdate().getMessage().getText();
        UserSession userSession = dispatchRequest.getUserSession();
        GroupTemplate template = (GroupTemplate) userSession.getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY);

        try {
            if(!messageText.equals("-")) {
                template.setYear(Integer.parseInt(messageText));
            } else {
                template.setYear(null);
            }
            return new HandlerResponse(null, true);
        } catch (Exception e) {
            return new HandlerResponse(telegramService.sendMessage(dispatchRequest.getChatId(), String.format(GroupTemplateConstants.WRONG_YEAR, messageText), replyKeyboardMarkup), false);
        }
    }
}
