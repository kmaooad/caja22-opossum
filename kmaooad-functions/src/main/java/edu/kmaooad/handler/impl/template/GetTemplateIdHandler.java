package edu.kmaooad.handler.impl.template;

import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupTemplateService;
import edu.kmaooad.service.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class GetTemplateIdHandler extends CommonRequestHandler {

    private final GroupTemplateService groupTemplateService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public GetTemplateIdHandler(GroupTemplateService groupTemplateService, TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.groupTemplateService = groupTemplateService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        UserSession userSession = dispatchRequest.getUserSession();
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        String messageText = dispatchRequest.getUpdate().getMessage().getText();

        GroupTemplate template = (GroupTemplate) userSession.getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY);
        String id = dispatchRequest.getUpdate().getMessage().getText();

        try {
            groupTemplateService.getTemplateById(id);
            template.setId(dispatchRequest.getUpdate().getMessage().getText());
            return new HandlerResponse(null, true);
        } catch (Exception e) {
            return new HandlerResponse(telegramService.sendMessage(dispatchRequest.getChatId(), String.format(GroupTemplateConstants.CANNOT_GET_TEMPLATE, messageText), replyKeyboardMarkup), false);
        }
    }
}
