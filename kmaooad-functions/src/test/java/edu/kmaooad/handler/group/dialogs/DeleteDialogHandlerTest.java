package edu.kmaooad.handler.group.dialogs;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.handler.impl.group.DeleteGroupDialog;
import edu.kmaooad.handler.impl.group.GroupButtonsHandler;
import edu.kmaooad.handler.impl.group.common.*;
import edu.kmaooad.model.Group;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.GroupService;
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
public class DeleteDialogHandlerTest {
    @InjectMocks
    private DeleteGroupDialog deleteGroupDialog;

    @Mock
    private AskGroupIDHandler askGroupIDHandler;
    @Mock
    private GetGroupIDHandler getGroupIDHandler;
    @Mock
    private GroupButtonsHandler groupButtonsHandler;
    @Mock
    private UserSessionService userSessionService;
    @Mock
    private GroupService groupService;
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
    public void testStartDialog(){
        final String message = "Dialog started";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        Mockito.when(askGroupIDHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));

        HandlerResponse response = deleteGroupDialog.startDialog(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }

    @Test
    public void testIsApplicable(){
        userSession.setDialogState(DialogState.DELETE_GROUP);
        Assertions.assertTrue(deleteGroupDialog.isApplicable(userRequest));
    }

    @Test
    public void testIsNotApplicableDialogState(){
        userSession.setDialogState(DialogState.UPDATE_GROUP);
        Assertions.assertFalse(deleteGroupDialog.isApplicable(userRequest));
    }
    //todo
    // take off comment
    @Test
    public void testFinishActionsSuccessfulGroupDelete(){
        final String message = "Dialog finish actions true";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_ID);
        Group group = new Group();
        group.setId("id");
        userSession.getData().put("group", group);

        Mockito.when(getGroupIDHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));
      //  Mockito.when(groupService.deleteGroup(Mockito.anyString())).thenReturn(true);

        HandlerResponse response = deleteGroupDialog.handle(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }
    //todo
    // take off comment
    @Test
    public void testFinishActionsUnsuccessfulGroupDelete(){
        final String message = "Dialog finish actions false";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_ID);
        Group group = new Group();
        group.setId("id");
        userSession.getData().put("group", group);

        Mockito.when(getGroupIDHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));
      //  Mockito.when(groupService.deleteGroup(Mockito.anyString())).thenReturn(false);

        HandlerResponse response = deleteGroupDialog.handle(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }

    @Test
    public void testDialogType(){
        Assertions.assertEquals(deleteGroupDialog.getDialogType(), DialogState.DELETE_GROUP);
    }
}
