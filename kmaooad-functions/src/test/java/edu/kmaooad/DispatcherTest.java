package edu.kmaooad;

import com.microsoft.azure.functions.HttpResponseMessage;
import edu.kmaooad.handler.UserRequestHandler;
import edu.kmaooad.handler.impl.CancelHandler;
import edu.kmaooad.model.UserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DispatcherTest {
    private Dispatcher dispatcher;
    @Mock
    private CancelHandler cancelHandler;

    final static SendMessage handleResult1 = new SendMessage();

    final static Update request1Update = new Update();
    final static Update request2Update = new Update();

    static {
        Message message = new Message();
        message.setText("❌ Скасувати");
        request1Update.setMessage(message);
    }

    final static UserRequest request1 = UserRequest
            .builder()
            .update(request1Update)
            .build();

    final static UserRequest request2 = UserRequest
            .builder()
            .update(request2Update)
            .build();

    @Before
    public void initTest() {
        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(handleResult1).when(cancelHandler).handle(request1);
        Mockito.doReturn(true).when(cancelHandler).isApplicable(request1);
        dispatcher = new Dispatcher(List.of(cancelHandler));
    }

    @Test
    public void testApplicableCancelHandler() {
        assertEquals(dispatcher.dispatch(request1), handleResult1);
    }

    @Test
    public void testNotApplicableCancelHandler() {
        assertEquals(dispatcher.dispatch(request2), null);
    }
}
