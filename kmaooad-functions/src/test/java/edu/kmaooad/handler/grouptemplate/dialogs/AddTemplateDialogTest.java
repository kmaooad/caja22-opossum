package edu.kmaooad.handler.grouptemplate.dialogs;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.handler.impl.template.button.TemplateButtonsHandler;
import edu.kmaooad.handler.impl.template.common.AskTemplateNameHandler;
import edu.kmaooad.handler.impl.template.common.AskTemplateYearHandler;
import edu.kmaooad.handler.impl.template.common.GetTemplateNameHandler;
import edu.kmaooad.handler.impl.template.common.GetTemplateYearHandler;
import edu.kmaooad.handler.impl.template.dialog.AddTemplateDialog;
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
public class AddTemplateDialogTest {
    @InjectMocks
    private AddTemplateDialog addTemplateDialog;

    @Mock
    private AskTemplateNameHandler askTemplateNameHandler;

    @Mock
    private GetTemplateNameHandler getTemplateNameHandler;

    @Mock
    private AskTemplateYearHandler askTemplateYearHandler;

    @Mock
    private GetTemplateYearHandler getTemplateYearHandler;

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
        Assertions.assertEquals(addTemplateDialog.getDialogType(), DialogState.ADD_GROUP_TEMPLATE);
    }

    @Test
    public void testStartDialog(){
        final String message = "Dialog started";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        Mockito.when(askTemplateNameHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));

        HandlerResponse response = addTemplateDialog.startDialog(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }

    @Test
    public void testIsApplicable(){
        userSession.setDialogState(DialogState.ADD_GROUP_TEMPLATE);
        Assertions.assertTrue(addTemplateDialog.isApplicable(userRequest));
    }

    @Test
    public void testIsNotApplicableDialogState(){
        userSession.setDialogState(DialogState.UPDATE_GROUP_TEMPLATE);
        Assertions.assertFalse(addTemplateDialog.isApplicable(userRequest));
    }

    @Test
    public void testFinishActionsSuccessfulTemplateAdd() {
        final String message = "Dialog finish actions true";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        userSession.setConversationState(ConversationState.WAITING_FOR_TEMPLATE_YEAR);
        userSession.getData().put("group template", new GroupTemplate());

        Mockito.when(getTemplateYearHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));

        HandlerResponse response = addTemplateDialog.handle(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }

    @Test
    public void testFinishActionsUnsuccessfulTemplateAdd() {
        final String message = "Dialog finish actions false";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        userSession.setConversationState(ConversationState.WAITING_FOR_TEMPLATE_YEAR);
        userSession.getData().put("group template", new GroupTemplate());

        Mockito.when(getTemplateYearHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));

        HandlerResponse response = addTemplateDialog.handle(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }
}
