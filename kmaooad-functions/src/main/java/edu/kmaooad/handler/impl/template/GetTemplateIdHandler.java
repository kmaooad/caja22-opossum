package edu.kmaooad.handler.impl.template;

import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import org.springframework.stereotype.Component;

@Component
public class GetTemplateIdHandler extends CommonRequestHandler {
    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        UserSession userSession = dispatchRequest.getUserSession();

        GroupTemplate template = (GroupTemplate) userSession.getData().get(GroupTemplateConstants.GROUP_TEMPLATE_MAP_KEY);
        template.setId(dispatchRequest.getUpdate().getMessage().getText());

        return new HandlerResponse(null, true);
    }
}
