package edu.kmaooad.handler.grouptemplate.dialogs;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.handler.impl.template.button.TemplateButtonsHandler;
import edu.kmaooad.handler.impl.template.common.*;
import edu.kmaooad.handler.impl.template.dialog.UpdateTemplateDialog;
import edu.kmaooad.model.*;
import edu.kmaooad.service.GroupTemplateService;
import edu.kmaooad.service.TelegramService;
import edu.kmaooad.service.UserSessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
public class UpdateTemplateDialogTest {

    @InjectMocks
    private UpdateTemplateDialog updateTemplateDialog;

    @Mock
    private AskTemplateNameHandler askTemplateNameHandler;

    @Mock
    private GetTemplateNameHandler getTemplateNameHandler;

    @Mock
    private AskTemplateYearHandler askTemplateYearHandler;

    @Mock
    private GetTemplateYearHandler getTemplateYearHandler;

    @Mock
    private AskTemplateIdHandler askTemplateIdHandler;

    @Mock
    private GetTemplateIdHandler getTemplateIdHandler;

    @Mock
    private TemplateButtonsHandler templateButtonsHandler;

    @Mock
    private UserSessionService userSessionService;

    @Mock
    private GroupTemplateService groupTemplateService;

    @Mock
    private TelegramService telegramService;

    private UserRequest userRequest;
    private UserSession userSession;
    private Update update;
    private Message message;

    @BeforeEach
    public void initTests(){
        userSession = UserSession.builder().data(new HashMap<>()).build();
        userRequest = UserRequest.builder().userSession(userSession).chatId(1L).build();
        message = new Message();
        update = new Update();
        update.setMessage(message);
    }

    @Test
    public void testDialogType(){
        Assertions.assertEquals(updateTemplateDialog.getDialogType(), DialogState.UPDATE_GROUP_TEMPLATE);
    }

    @Test
    public void testStartDialog(){
        final String message = "Dialog started";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        Mockito.when(askTemplateIdHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));

        HandlerResponse response = updateTemplateDialog.startDialog(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }

    @Test
    public void testIsApplicable(){
        userSession.setDialogState(DialogState.UPDATE_GROUP_TEMPLATE);
        Assertions.assertTrue(updateTemplateDialog.isApplicable(userRequest));
    }

    @Test
    public void testIsNotApplicableDialogState(){
        userSession.setDialogState(DialogState.UPDATE_GROUP);
        Assertions.assertFalse(updateTemplateDialog.isApplicable(userRequest));
    }

    @Test
    public void testFinishActionsSuccessfulGroupUpdate(){
        final String message = "Dialog finish actions true";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        userSession.setConversationState(ConversationState.WAITING_FOR_TEMPLATE_YEAR);
        userSession.getData().put("group template", new GroupTemplate());

        Mockito.when(getTemplateYearHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));

        HandlerResponse response = updateTemplateDialog.handle(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }

    @Test
    public void testFinishActionsUnsuccessfulGroupUpdate(){
        final String message = "Dialog finish actions false";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        userSession.setConversationState(ConversationState.WAITING_FOR_TEMPLATE_YEAR);
        userSession.getData().put("group template", new GroupTemplate());

        Mockito.when(getTemplateYearHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));

        HandlerResponse response = updateTemplateDialog.handle(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }
}
