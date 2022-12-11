package edu.kmaooad.handler.impl.group.assign;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.ActivityService;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@Slf4j
public class SelectActivitiesToAssignToGroupHandler implements ButtonRequestHandler {

    private final KeyboardHelper keyboardHelper;
    private final GroupService groupService;
    private final ActivityService activityService;
    private final TelegramService telegramService;

    SelectActivitiesToAssignToGroupHandler(KeyboardHelper keyboardHelper, TelegramService telegramService,
                                           GroupService groupService, ActivityService activityService
    ) {
        this.keyboardHelper = keyboardHelper;
        this.telegramService = telegramService;
        this.groupService = groupService;
        this.activityService = activityService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserRequestHandler.isTextMessage(userRequest.getUpdate()) &&
                userRequest.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_GROUP_TO_ASSIGN_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest userRequest) {
        log.info(userRequest.toString());
        String text = userRequest.getUpdate().getMessage().getText();
        Group group = groupService.getGroupByName(text.trim());
        log.info("User picked group by name " + text + ", found group " + group);
        userRequest.getUserSession().getData().put(GroupConstants.GROUP_MAP_KEY, group);
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildAdditionalActionsVertical(activityService.getStatusOfActivitiesForGroup(group));
        userRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_GROUP_ACTIVITY_ASSIGN_CHOICE);

        return new HandlerResponse(telegramService.sendMessage(userRequest.getChatId(), "Оберіть активність яку хочете додати до " + group.getName() + " ⤵️", replyKeyboardMarkup), true);
    }
}
