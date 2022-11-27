package edu.kmaooad.handler.impl.group.create;

import edu.kmaooad.constants.bot.BotMessageEnum;
import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.handler.impl.group.GroupButtonsHandler;
import edu.kmaooad.handler.impl.group.GroupConstants;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@Slf4j
public class GetGroupYearHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final GroupService groupService;
    private final GroupButtonsHandler groupButtonsHandler;

    public GetGroupYearHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService, GroupService groupService, GroupButtonsHandler groupButtonsHandler) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.groupService = groupService;
        this.groupButtonsHandler = groupButtonsHandler;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate()) &&
                DialogState.ADD_GROUP.equals(userRequest.getUserSession().getDialogState()) && ConversationState.WAITING_FOR_GROUP_YEAR.equals(userRequest.getUserSession().getConversationState());
    }

    @Override
    public BotApiMethod<?> handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();

        String messageText = dispatchRequest.getUpdate().getMessage().getText();
        UserSession userSession = dispatchRequest.getUserSession();
        Group group = (Group) userSession.getData().get(GroupConstants.GROUP_MAP_KEY);

        try {
            group.setYear(Integer.parseInt(messageText));
        } catch (Exception e) {
            //Do not make any changes. Year text incorrect.
            return telegramService.sendMessage(dispatchRequest.getChatId(), String.format(GroupConstants.WRONG_YEAR, messageText), replyKeyboardMarkup);
        }

        log.warn(group.toString());
        if (groupService.addGroup(group)) {
            userSession.setDialogState(null);
            userSession.setConversationState(null);
            userSession.getData().remove(GroupConstants.GROUP_MAP_KEY);
            telegramService.sendMessage(dispatchRequest.getChatId(), BotMessageEnum.SUCCESSFULLY_ADD.getMessage());
            return this.groupButtonsHandler.handle(dispatchRequest);
        } else {
            return telegramService.sendMessage(dispatchRequest.getChatId(), BotMessageEnum.EXCEPTION_UNKNOWN.getMessage());
        }
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
