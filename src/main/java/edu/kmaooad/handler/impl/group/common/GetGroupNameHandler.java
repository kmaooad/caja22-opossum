package edu.kmaooad.handler.impl.group.common;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Arrays;

@Component
@Slf4j
public class GetGroupNameHandler extends CommonRequestHandler {
    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        UserSession userSession = dispatchRequest.getUserSession();

        Group group = (Group) userSession.getData().get(GroupConstants.GROUP_MAP_KEY);
        group.setName(dispatchRequest.getUpdate().getMessage().getText());
        log.info("Group name set");

        return new HandlerResponse(null, true);
    }
}
