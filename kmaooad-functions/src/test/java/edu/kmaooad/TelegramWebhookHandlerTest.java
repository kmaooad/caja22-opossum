package edu.kmaooad;


import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import edu.kmaooad.functions.TelegramWebhookHandler;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TelegramWebhookHandlerTest {
    private HttpRequestMessage<Optional<String>> getRequest(Optional<String> queryBody) {
        // Setup
        @SuppressWarnings("unchecked") final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

        doReturn(queryBody).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        return req;
    }

    private ExecutionContext getContext() {
        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        return context;
    }

    @Test
    public void testInvalidStringReturnsBadRequest() {
        // Invoke
        final HttpResponseMessage ret = new TelegramWebhookHandler().run(getRequest(Optional.of("Bad request")), getContext());

        // Verify
        assertEquals(ret.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testValidJSONReturnsOK() {
        // Invoke
        final TelegramWebhookHandler spyHandler = spy(TelegramWebhookHandler.class);
        doReturn(null).when(spyHandler).handleRequest(any(Update.class), any(ExecutionContext.class));

        final HttpResponseMessage ret = spyHandler.run(getRequest(Optional.of("{\"update_id\":22483710,\n" + "\"message\":{\"message_id\":7,\"from\":{\"id\":463933526,\"is_bot\":false,\"first_name\":\"A\",\"last_name\":\"B\",\"username\":\"C\",\"language_code\":\"en\"},\"chat\":{\"id\":463933526,\"first_name\":\"A\",\"last_name\":\"B\",\"username\":\"V\",\"type\":\"private\"},\"date\":1664719220,\"text\":\"Very interesting topic.\"}}")), getContext());

        // Verify
        assertEquals(ret.getStatus(), HttpStatus.OK);
    }
}
