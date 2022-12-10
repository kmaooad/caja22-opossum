package edu.kmaooad.handler.impl.template.dialog;

import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.handler.DialogHandler;
import edu.kmaooad.handler.impl.template.button.TemplateButtonsHandler;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.service.GroupTemplateService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@Slf4j
public class UpdateTemplateDialog extends DialogHandler {
    private final UserSessionService userSessionService;
    private final GroupTemplateService templateService;
    private final TelegramService telegramService;
    private final TemplateButtonsHandler templateButtonsHandler;

    public UpdateTemplateDialog(UserSessionService userSessionService, GroupTemplateService templateService,
                                TelegramService telegramService, TemplateButtonsHandler templateButtonsHandler) {
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

        // TODO: complete method
    }

    @Override
    protected void finishActions(UserRequest dispatchRequest) {

    }

    @Override
    public DialogState getDialogType() {
        return DialogState.UPDATE_GROUP_TEMPLATE;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return request.getUserSession().getDialogState().equals(getDialogType());
    }
}
