package edu.kmaooad;

import edu.kmaooad.constants.bot.GlobalConstants;
import edu.kmaooad.handler.DialogHandler;
import edu.kmaooad.handler.impl.CancelHandler;
import edu.kmaooad.model.HandlerResponse;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.UserSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DispatcherTest {
    private Dispatcher dispatcher;
    @Mock
    private CancelHandler cancelHandler;

    private final static UserSession userSession = UserSession.builder().build();
    private final static HandlerResponse handleResult1 = new HandlerResponse(new SendMessage(), true);

    private final static Update request1Update = new Update();
    private final static Update request2Update = new Update();

    static {
        Message message = new Message();
        message.setText(GlobalConstants.CANCEL_BUTTON_LABEL);
        request1Update.setMessage(message);
    }

    final static UserRequest request1 = UserRequest
            .builder()
            .update(request1Update)
            .build();

    final static UserRequest request2 = UserRequest
            .builder()
            .userSession(userSession)
            .update(request2Update)
            .build();

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(handleResult1).when(cancelHandler).handle(request1);
        Mockito.doReturn(true).when(cancelHandler).isApplicable(request1);
        dispatcher = new Dispatcher(List.of(cancelHandler), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void testApplicableCancelHandler() {
        assertEquals(dispatcher.dispatch(request1), handleResult1);
    }

    @Test
    public void testNotApplicableCancelHandler() {
        assertEquals(dispatcher.dispatch(request2), new HandlerResponse(null, false));
    }
}
