package edu.kmaooad.handler.impl.group.assign;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.helper.KeyboardHelper;
import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.ActivityService;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.ServiceException;
import edu.kmaooad.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static edu.kmaooad.constants.bot.GlobalConstants.ASSIGNED;
import static edu.kmaooad.constants.bot.GroupConstants.GROUP_MAP_KEY;

@Component
@Slf4j
public class AssignActivitiesToGroupHandler implements ButtonRequestHandler {


    private final KeyboardHelper keyboardHelper;
    private final GroupService groupService;
    private final ActivityService activityService;
    private final TelegramService telegramService;

    AssignActivitiesToGroupHandler(KeyboardHelper keyboardHelper, TelegramService telegramService,
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
                userRequest.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_GROUP_ACTIVITY_ASSIGN_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest userRequest) {
        Group group = (Group) userRequest.getUserSession().getData().get(GroupConstants.GROUP_MAP_KEY);
        String text = userRequest.getUpdate().getMessage().getText();
        log.info("Trying to assign/unassign activity " + text + " from group");
        try {
            if (text.contains(ASSIGNED)) {
                String activityName = text.replace(ASSIGNED, "").trim();
                Activity activity = activityService.getActivityByName(activityName);
                groupService.deleteActivityGroup(activity.getId(), group.getId());
                telegramService.sendMessage(userRequest.getChatId(),
                        "Видалена активність " + activity.getName() + " з групи " + group.getName());
                log.info("Deleted activity " + activityName + " from group " + group.getName());
            } else {
                Activity activity = activityService.getActivityByName(text);
                groupService.addActivityGroup(activity.getId(), group.getId());
                telegramService.sendMessage(userRequest.getChatId(),
                        "Додана активність " + activity.getName() + " до групи " + group.getName());
                log.info("Added activity " + activity.getName() + " to group " + group.getName());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("Failed to delete or add activity " + text + " to group" + group.getName());
        }

        Group updatedGroup = groupService.getGroupById(group.getId());

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildAdditionalActionsVertical(activityService.getStatusOfActivitiesForGroup(updatedGroup));
        userRequest.getUserSession().getData().put(GROUP_MAP_KEY, updatedGroup);

        return new HandlerResponse(telegramService.sendMessage(userRequest.getChatId(), "Оберіть активність яку хочете додати до " + updatedGroup.getName() + " ⤵️", replyKeyboardMarkup), true);
    }
}