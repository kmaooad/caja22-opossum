package edu.kmaooad.handler.grouptemplate.buttons;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.GroupConstants;
import edu.kmaooad.constants.bot.GroupTemplateConstants;
import edu.kmaooad.handler.impl.template.button.ShowAllGroupTemplatesButtonHandler;
import edu.kmaooad.handler.impl.template.button.TemplateButtonsHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupService;
import edu.kmaooad.service.GroupTemplateService;
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
public class ShowAllGroupTemplatesButtonHandlerTest {
    @InjectMocks
    private ShowAllGroupTemplatesButtonHandler showAllGroupTemplatesButtonHandler;

    @Mock
    private TemplateButtonsHandler templateButtonsHandler;

    @Mock
    private GroupTemplateService templateService;

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
    }

    @Test
    public void testIsNotApplicableConversationState(){
        message.setText(GroupTemplateConstants.SHOW_GROUP_TEMPLATE);
        userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
        Assertions.assertFalse(showAllGroupTemplatesButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsNotApplicableMessage(){
        message.setText("Not applicable");
        userSession.setConversationState(ConversationState.WAITING_FOR_TEMPLATE_ACTION_CHOICE);
        Assertions.assertFalse(showAllGroupTemplatesButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testIsApplicable(){
        message.setText(GroupTemplateConstants.GROUP_TEMPLATE_SHOW_ALL_BUTTON_LABEL);
        userSession.setConversationState(ConversationState.WAITING_FOR_TEMPLATE_ACTION_CHOICE);
        Assertions.assertTrue(showAllGroupTemplatesButtonHandler.isApplicable(userRequest));
    }

    @Test
    public void testHandler(){
        Mockito.when(templateButtonsHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(null, true));
        HandlerResponse handlerResponse = showAllGroupTemplatesButtonHandler.handle(userRequest);

        Assertions.assertNull(handlerResponse.getResult());
        Assertions.assertTrue(handlerResponse.isSuccess());
    }
}
