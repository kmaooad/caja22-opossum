package edu.kmaooad;


import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import edu.kmaooad.functions.TelegramWebhookHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TelegramWebhookHandlerTest {

    @Mock
    HttpRequestMessage<Optional<String>> mockRequest;

    @Mock
    ExecutionContext mockContext;

    @Spy
    TelegramWebhookHandler spyHandler;


    @BeforeEach
    public void initMocks() {
        doAnswer((Answer<HttpResponseMessage.Builder>) invocation -> {
            HttpStatus status = (HttpStatus) invocation.getArguments()[0];
            return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
        }).when(mockRequest).createResponseBuilder(any(HttpStatus.class));

        doReturn(Logger.getGlobal()).when(mockContext).getLogger();
    }

    @Test
    public void testInvalidStringReturnsBadRequest() {
        doReturn(Optional.of("Bad request")).when(mockRequest).getBody();

        final HttpResponseMessage ret = new TelegramWebhookHandler().run(mockRequest, mockContext);

        assertEquals(ret.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testValidJSONReturnsOK() {
        doReturn(Optional.of("{\"update_id\":22483710,\n" + "\"message\":{\"message_id\":7,\"from\":{\"id\":463933526,\"is_bot\":false,\"first_name\":\"A\",\"last_name\":\"B\",\"username\":\"C\",\"language_code\":\"en\"},\"chat\":{\"id\":463933526,\"first_name\":\"A\",\"last_name\":\"B\",\"username\":\"V\",\"type\":\"private\"},\"date\":1664719220,\"text\":\"Very interesting topic.\"}}"))
                .when(mockRequest).getBody();
        doReturn(null).when(spyHandler).handleRequest(any(Update.class), any(ExecutionContext.class));

        final HttpResponseMessage ret = spyHandler.run(mockRequest, mockContext);

        assertEquals(ret.getStatus(), HttpStatus.OK);
    }
}
