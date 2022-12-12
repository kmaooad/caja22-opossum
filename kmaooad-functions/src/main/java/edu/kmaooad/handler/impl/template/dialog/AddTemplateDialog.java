package edu.kmaooad.handler.impl.template.dialog;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.DialogHandler;
import edu.kmaooad.handler.impl.template.button.TemplateButtonsHandler;
import edu.kmaooad.handler.impl.template.common.*;
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

@Component
@Slf4j
public class AddTemplateDialog extends DialogHandler {
    private final UserSessionService userSessionService;
    private final GroupTemplateService templateService;
    private final TelegramService telegramService;
    private final TemplateButtonsHandler templateButtonsHandler;

    public AddTemplateDialog(UserSessionService userSessionService, GroupTemplateService templateService,
                             TelegramService telegramService, TemplateButtonsHandler templateButtonsHandler,
                             AskTemplateNameHandler askTemplateNameHandler, GetTemplateNameHandler getTemplateNameHandler,
                             AskTemplateGradeHandler askTemplateGradeHandler, GetTemplateGradeHandler getTemplateGradeHandler,
                             AskTemplateYearHandler askTemplateYearHandler, GetTemplateYearHandler getTemplateYearHandler) {
        this.userSessionService = userSessionService;
        this.templateService = templateService;
        this.telegramService = telegramService;
        this.templateButtonsHandler = templateButtonsHandler;

        handlers = new LinkedHashMap<>();

        PostActions moveToNext = (request, response) -> {
            if (response.isSuccess()) {
                return handle(request);
            }
            return response;
        };

        handlers.put(ConversationState.ASK_FOR_TEMPLATE_NAME, new DialogSingleHandler(askTemplateNameHandler));
        handlers.put(ConversationState.WAITING_FOR_TEMPLATE_NAME, new DialogSingleHandler(getTemplateNameHandler, moveToNext));

        handlers.put(ConversationState.ASK_FOR_TEMPLATE_GRADE, new DialogSingleHandler(askTemplateGradeHandler));
        handlers.put(ConversationState.WAITING_FOR_TEMPLATE_GRADE, new DialogSingleHandler(getTemplateGradeHandler, moveToNext));

        handlers.put(ConversationState.ASK_FOR_TEMPLATE_YEAR, new DialogSingleHandler(askTemplateYearHandler));
        handlers.put(ConversationState.WAITING_FOR_TEMPLATE_YEAR, new DialogSingleHandler(getTemplateYearHandler));
    }

    @Override
    public final HandlerResponse startDialog(UserRequest userRequest) {
        userRequest.getUserSession().getData().put(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY, new GroupTemplate());
        userSessionService.saveSession(userRequest.getChatId(), userRequest.getUserSession());
        log.info("Some response: " + userRequest.getUserSession().getData().keySet());
        return super.startDialog(userRequest);
    }

    @Override
    protected void finishActions(UserRequest dispatchRequest) {
        log.warn("AddTemplateDialog finish actions: " + dispatchRequest
                .getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY));

        GroupTemplate template = (GroupTemplate) dispatchRequest
                .getUserSession().getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY);

        try {
            templateService.addGroupTemplate(template);
            telegramService.sendMessage(dispatchRequest.getChatId(), GroupTemplateConstants.TEMPLATE_SUCCESSFULLY_ADDED);
        } catch(ServiceException e) {
            log.error("Cannot add template: " + template);
            telegramService.sendMessage(dispatchRequest.getChatId(), GroupTemplateConstants.CANNOT_CREATE_EMPTY_TEMPLATE);
        }

        dispatchRequest.getUserSession().setDialogState(null);
        dispatchRequest.getUserSession().setConversationState(ConversationState.WAITING_FOR_MAIN_MENU_ACTION_CHOICE);
        templateButtonsHandler.handle(dispatchRequest);
    }

    @Override
    public DialogState getDialogType() {
        return DialogState.ADD_GROUP_TEMPLATE;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return request.getUserSession().getDialogState().equals(getDialogType());
    }
}
