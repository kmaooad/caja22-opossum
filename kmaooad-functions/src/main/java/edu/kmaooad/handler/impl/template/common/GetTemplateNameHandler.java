package edu.kmaooad.handler.impl.template.common;

import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.model.*;
import org.springframework.stereotype.Component;

@Component
public class GetTemplateNameHandler extends CommonRequestHandler {
    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        UserSession userSession = dispatchRequest.getUserSession();

        GroupTemplate template = (GroupTemplate) userSession.getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY);
        String messageText = dispatchRequest.getUpdate().getMessage().getText();
        if(!messageText.equals("-")) {
            template.setName(messageText);
        } else {
            template.setName(null);
        }

        return new HandlerResponse(null, true);
    }
}
