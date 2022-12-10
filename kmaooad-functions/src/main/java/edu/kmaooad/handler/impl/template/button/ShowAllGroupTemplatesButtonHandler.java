package edu.kmaooad.handler.impl.template.button;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.ButtonRequestHandler;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.GroupTemplateService;
import edu.kmaooad.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ShowAllGroupTemplatesButtonHandler implements ButtonRequestHandler {
    private final GroupTemplateService templateService;

    private final TelegramService telegramService;
    private final TemplateButtonsHandler templateButtonsHandler;

    public ShowAllGroupTemplatesButtonHandler(GroupTemplateService templateService, TelegramService telegramService,
                                              TemplateButtonsHandler templateButtonsHandler) {
        this.templateService = templateService;
        this.telegramService = telegramService;
        this.templateButtonsHandler = templateButtonsHandler;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return UserRequestHandler.isTextMessage(request.getUpdate(), GroupTemplateConstants.GROUP_TEMPLATE_SHOW_ALL_BUTTON_LABEL) &&
                request.getUserSession().getConversationState().equals(ConversationState.WAITING_FOR_TEMPLATE_ACTION_CHOICE);
    }

    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        List<GroupTemplate> templates = templateService.getAllTemplates();
        log.info("Templates: " + templates.toString());

        for (GroupTemplate template : templates) {
            telegramService.sendMessage(dispatchRequest.getChatId(),
                    String.format(GroupTemplateConstants.SHOW_GROUP_TEMPLATE,
                            template.getName(), template.getGrade(), template.getYear()));
        }

        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        return templateButtonsHandler.handle(dispatchRequest);
    }
}
