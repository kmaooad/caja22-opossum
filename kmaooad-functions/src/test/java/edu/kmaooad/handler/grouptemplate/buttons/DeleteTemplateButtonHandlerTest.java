package edu.kmaooad.handler.grouptemplate.buttons;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.impl.template.button.DeleteTemplateButtonHandler;
import edu.kmaooad.handler.impl.template.dialog.DeleteTemplateDialog;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeleteTemplateButtonHandlerTest {
    @InjectMocks
    private DeleteTemplateButtonHandler deleteTemplateButtonHandler;

    @Mock
    private DeleteTemplateDialog deleteTemplateDialog;

    private Message message;
    private Update update;
    private UserSession userSession;
    private UserRequest userRequest;

    @BeforeEach
    public void initTests(){
        message = new Message();
        update = new Update();
        update.setMessage(message);
        userSession = UserSession.builder()
                .chatId(1L)
                .build();
        userRequest = UserRequest.builder().update(update).userSession(userSession).build();

        Mockito.when(deleteTemplateDialog.startDialog(Mockito.any(UserRequest.class)))
                .thenReturn(new HandlerResponse(null, true));
    }

    @Test
    public void testIsNotApplicableConversationState(){
        message.setText(GroupTemplateConstants.GROUP_TEMPLATE_DELETE_BUTTON_LABEL);
        userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
        Assertions.assertFalse(deleteTemplateButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsNotApplicableMessage(){
        message.setText("Not applicable");
        userSession.setConversationState(ConversationState.WAITING_FOR_TEMPLATE_ACTION_CHOICE);
        Assertions.assertFalse(deleteTemplateButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsApplicable(){
        message.setText(GroupTemplateConstants.GROUP_TEMPLATE_DELETE_BUTTON_LABEL);
        userSession.setConversationState(ConversationState.WAITING_FOR_TEMPLATE_ACTION_CHOICE);
        Assertions.assertTrue(deleteTemplateButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testHandler(){
        HandlerResponse handlerResponse = deleteTemplateButtonHandler.handle(userRequest);

        Assertions.assertNull(handlerResponse.getResult());
        Assertions.assertTrue(handlerResponse.isSuccess());
    }
}
