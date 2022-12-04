package edu.kmaooad.handler.impl.group.common;

import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class GetGroupGradeHandler extends CommonRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public GetGroupGradeHandler(TelegramService telegramService, KeyboardHelper keyboardHelper) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }
    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();

        String messageText = dispatchRequest.getUpdate().getMessage().getText();
        try {
            UserSession userSession = dispatchRequest.getUserSession();

            Group group = (Group) userSession.getData().get(GroupConstants.GROUP_MAP_KEY);
            group.setGrade(Integer.parseInt(messageText));

            return new HandlerResponse(null, true);
        } catch (Exception e){
            //Do not make any changes. Grade text incorrect.
            return new HandlerResponse(telegramService.sendMessage(dispatchRequest.getChatId(), String.format(GroupConstants.WRONG_GRADE, messageText), replyKeyboardMarkup), false);
        }
    }
}
