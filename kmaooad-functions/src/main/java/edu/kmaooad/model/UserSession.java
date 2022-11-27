package edu.kmaooad.model;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class UserSession {
    private Long chatId;
    private ConversationState conversationState;
    private DialogState dialogState;

    /**
     * All data that moves through the dialog stores here
     */
    @Setter(AccessLevel.NONE)
    private Map<String, Object> data;
}
