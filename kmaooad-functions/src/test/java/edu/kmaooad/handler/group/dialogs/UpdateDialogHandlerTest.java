package edu.kmaooad.handler.group.dialogs;

import edu.kmaooad.constants.bot.ConversationState;
import edu.kmaooad.constants.bot.DialogState;
import edu.kmaooad.handler.impl.group.UpdateGroupDialog;
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
public class UpdateDialogHandlerTest {
    @InjectMocks
    private UpdateGroupDialog updateGroupDialog;

    @Mock
    private AskGroupIDHandler askGroupIDHandler;
    @Mock
    private GetGroupIDHandler getGroupIDHandler;
    @Mock
    private AskGroupNameHandler askGroupNameHandler;
    @Mock
    private GetGroupNameHandler getGroupNameHandler;
    @Mock
    private AskGroupGradeHandler askGroupGradeHandler;
    @Mock
    private GetGroupGradeHandler getGroupGradeHandler;
    @Mock
    private AskGroupYearHandler askGroupYearHandler;
    @Mock
    private GetGroupYearHandler getGroupYearHandler;
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

        HandlerResponse response = updateGroupDialog.startDialog(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }

    @Test
    public void testIsApplicable(){
        userSession.setDialogState(DialogState.UPDATE_GROUP);
        Assertions.assertTrue(updateGroupDialog.isApplicable(userRequest));
    }

    @Test
    public void testIsNotApplicableDialogState(){
        userSession.setDialogState(DialogState.ADD_GROUP);
        Assertions.assertFalse(updateGroupDialog.isApplicable(userRequest));
    }
    //todo
    // take off comment
    @Test
    public void testFinishActionsSuccessfulGroupUpdate(){
        final String message = "Dialog finish actions true";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_YEAR);
        userSession.getData().put("group", new Group());

        Mockito.when(getGroupYearHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));
     //   Mockito.when(groupService.updateGroup(Mockito.any(Group.class))).thenReturn(true);

        HandlerResponse response = updateGroupDialog.handle(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }
    //todo
    // take off comment
    @Test
    public void testFinishActionsUnsuccessfulGroupUpdate(){
        final String message = "Dialog finish actions false";
        SendMessage res = SendMessage.builder().chatId(userRequest.getChatId()).text(message).build();
        userSession.setConversationState(ConversationState.WAITING_FOR_GROUP_YEAR);
        userSession.getData().put("group", new Group());

        Mockito.when(getGroupYearHandler.handle(Mockito.any(UserRequest.class))).thenReturn(new HandlerResponse(res, true));
     //   Mockito.when(groupService.updateGroup(Mockito.any(Group.class))).thenReturn(false);

        HandlerResponse response = updateGroupDialog.handle(userRequest);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(response.getResult(), res);
    }

    @Test
    public void testDialogType(){
        Assertions.assertEquals(updateGroupDialog.getDialogType(), DialogState.UPDATE_GROUP);
    }
}
