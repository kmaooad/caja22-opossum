package edu.kmaooad.handler.impl.group.assign;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SelectGroupToAssignHandler implements ButtonRequestHandler {

    private final KeyboardHelper keyboardHelper;
    private final GroupService groupService;
    private final TelegramService telegramService;

    SelectGroupToAssignHandler(KeyboardHelper keyboardHelper, GroupService groupService,
                               TelegramService telegramService) {
        this.keyboardHelper = keyboardHelper;
        this.groupService = groupService;
        this.telegramService = telegramService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate(), GroupConstants.GROUP_SHOW_ALL_ASSIGN_LABEL) &&
                userRequest.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_GROUP_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest userRequest) {
        List<String> groupNames = groupService.getAllGroups().stream()
                .map(Group::getName).collect(Collectors.toList());

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildAdditionalActionsVertical(groupNames);
        userRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_GROUP_TO_ASSIGN_CHOICE);

        return new HandlerResponse(telegramService.sendMessage(userRequest.getChatId(), "Оберіть групу якій хочете додати активності⤵️", replyKeyboardMarkup), true);

    }
}
