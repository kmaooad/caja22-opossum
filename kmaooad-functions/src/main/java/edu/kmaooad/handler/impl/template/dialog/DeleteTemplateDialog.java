package edu.kmaooad.handler.impl.template.dialog;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.DialogHandler;
import edu.kmaooad.handler.impl.template.AskTemplateIdHandler;
import edu.kmaooad.handler.impl.template.GetTemplateIdHandler;
import edu.kmaooad.handler.impl.template.button.TemplateButtonsHandler;
import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.GroupTemplateService;
import edu.kmaooad.service.ServiceException;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Component
@Slf4j
public class DeleteTemplateDialog extends DialogHandler {
    private final UserSessionService userSessionService;
    private final GroupTemplateService templateService;
    private final TelegramService telegramService;
    private final TemplateButtonsHandler templateButtonsHandler;

    public DeleteTemplateDialog(UserSessionService userSessionService, TelegramService telegramService,
                                GroupTemplateService templateService, TemplateButtonsHandler templateButtonsHandler,
                                AskTemplateIdHandler askTemplateIdHandler, GetTemplateIdHandler getTemplateIdHandler) {
        this.userSessionService = userSessionService;
        this.templateService = templateService;
        this.telegramService = telegramService;
        this.templateButtonsHandler = templateButtonsHandler;

        handlers = new LinkedHashMap<>();

        handlers.put(ConversationState.ASK_FOR_TEMPLATE_ID, new DialogSingleHandler(askTemplateIdHandler));
        handlers.put(ConversationState.WAITING_FOR_TEMPLATE_ID, new DialogSingleHandler(getTemplateIdHandler));
    }

    @Override
    public final HandlerResponse startDialog(UserRequest userRequest) {
        List<GroupTemplate> templates = templateService.getAllTemplates();
        log.info("Templates: " + templates.toString());

        for (GroupTemplate template : templates) {
            telegramService.sendMessage(userRequest.getChatId(),
                    String.format(GroupTemplateConstants.SHOW_GROUP_TEMPLATE_WITH_ID,
                            template.getName() == null ? "" : template.getName(),
                            template.getGrade() == null ? "" : template.getGrade(),
                            template.getYear() == null ? "" : template.getYear(),
                            template.getId())
            );
        }

        userRequest.getUserSession().getData().put(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY, new GroupTemplate());
        userSessionService.saveSession(userRequest.getChatId(), userRequest.getUserSession());
        log.info("Some response: " + userRequest.getUserSession().getData().keySet());
        return super.startDialog(userRequest);
    }

    @Override
    protected void finishActions(UserRequest dispatchRequest) {
        GroupTemplate template = (GroupTemplate) dispatchRequest.getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY);
        try {
            templateService.deleteGroupTemplate(template.getId());
            telegramService.sendMessage(dispatchRequest.getChatId(), GroupTemplateConstants.TEMPLATE_SUCCESSFULLY_DELETED);
        } catch(ServiceException e) {
            log.error("Cannot add template: " + template);
            telegramService.sendMessage(dispatchRequest.getChatId(), e.getMessage());
        }

        dispatchRequest.getUserSession().setDialogState(null);
        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        templateButtonsHandler.handle(dispatchRequest);
    }

    @Override
    public DialogState getDialogType() {
        return DialogState.DELETE_GROUP_TEMPLATE;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return request.getUserSession().getDialogState().equals(getDialogType());
    }
}
