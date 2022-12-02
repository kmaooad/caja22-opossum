package edu.kmaooad.handler.impl.group.common;

import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.handler.CommonRequestHandler;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetGroupIDHandler extends CommonRequestHandler {
    @Override
    public HandlerResponse handle(UserRequest dispatchRequest) {
        UserSession userSession = dispatchRequest.getUserSession();

        Group group = (Group) userSession.getData().get(GroupConstants.GROUP_MAP_KEY);
        group.setId(dispatchRequest.getUpdate().getMessage().getText());

        return new HandlerResponse(null, true);
    }
}
