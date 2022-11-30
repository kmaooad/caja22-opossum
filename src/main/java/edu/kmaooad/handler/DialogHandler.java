package edu.kmaooad.handler;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The class handles the whole dialog entity.
 * It contains map which stores handlers which will be used during specific state of a dialog.
 */
@Slf4j
public abstract class DialogHandler implements UserRequestHandler {
    protected LinkedHashMap<ConversationState, DialogSingleHandler> handlers;

    /**
     * This method make actions after dialog has finished.
     *
     * @param dispatchRequest request processed in the last handler
     */
    protected abstract void finishActions(UserRequest dispatchRequest);

    /**
     * This method defines how dialog should be set
     */
    public HandlerResponse startDialog(UserRequest request) {
        UserSession userSession = request.getUserSession();
        userSession.setDialogState(getDialogType());

        List<ConversationState> states = new ArrayList<>(handlers.keySet());
        if (states.isEmpty()) {
            log.error("Conversation state is empty for the dialog");
        }
        userSession.setConversationState(states.get(0));
        return handle(request);
    }

    /**
     * Get type of the dialog.
     * @return type of the dialog.*/
    public abstract DialogState getDialogType();

    /**
     * Find next state
     *
     * @param current Current state
     * @return next state or null if current one is final
     * @throws IllegalArgumentException if passed state is not in handlers
     */
    protected final ConversationState findNextState(ConversationState current) {
        List<ConversationState> states = new ArrayList<>(handlers.keySet());
        log.info(String.valueOf(current));
        //Iterate to the second last element to avoid indexing out of states
        for (int i = 0; i < states.size() - 1; i++) {
            if (states.get(i).equals(current)) {
                return states.get(i + 1);
            }
        }
        if (states.get(states.size() - 1).equals(current)) {
            //Current is the last one state
            return null;
        } else {
            throw new IllegalArgumentException("There is no passed state in the handlers map");
        }
    }

    @Override
    public final HandlerResponse handle(UserRequest dispatchRequest) {
        log.info("User session: " + dispatchRequest.getUserSession());
        ConversationState currentState = dispatchRequest.getUserSession().getConversationState();
        ConversationState nextState = findNextState(currentState);
        DialogSingleHandler handler = handlers.get(currentState);
        UserSession userSession = dispatchRequest.getUserSession();

        HandlerResponse res = handler.userRequestHandler.handle(dispatchRequest);

        if (nextState == null) {
            //Actions for the last step
            finishActions(dispatchRequest);
        }

        //Set next state if current handler was successful
        if (res.isSuccess()) {
            userSession.setConversationState(nextState);
        }

        return handler.callback.method(dispatchRequest, res);
    }

    @Data
    @AllArgsConstructor
    public static class DialogSingleHandler {
        UserRequestHandler userRequestHandler;
        PostActions callback;
        public DialogSingleHandler(UserRequestHandler userRequestHandler){
            this(userRequestHandler, (request, response) -> response);
        }
    }

    /**
     * This is used to make some actions after request finished.
     * You can run another handler or do some logging
     */
    @FunctionalInterface
    public interface PostActions {
        HandlerResponse method(UserRequest request, HandlerResponse response);
    }

}