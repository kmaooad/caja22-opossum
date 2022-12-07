package edu.kmaooad.handler.impl.template;

import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.TelegramService;
import org.springframework.stereotype.Component;

@Component
public class AskTemplateNameHandler extends CommonRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public AskTemplateNameHandler(TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        return new HandlerResponse(telegramService.sendMessage(dispatchRequest.getChatId(),
                message(), keyboardHelper.buildMenuWithCancel()), true);
    }

    private String message() {
        return GroupTemplateConstants.ASK_FOR_GROUP_TEMPLATE_NAME + "\n\n"
                + GlobalConstants.SKIP_STEP;
    }
}
