package edu.kmaooad.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import edu.kmaooad.telegram.StudentsBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class TelegramWebhookHandler extends FunctionInvoker<Update, BotApiMethod<?>> {
    /**
     * This function listens at endpoint "/api/TelegramWebhook". To invoke it using
     * "curl" command in bash:
     * curl -d "HTTP Body" {your host}/api/TelegramWebhook
     */
    @FunctionName("TelegramWebhook")
    public HttpResponseMessage run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.FUNCTION)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Java HTTP trigger processed a request.");

        try {
            String req = request.getBody().orElse(null);

            Update update = new ObjectMapper().readerFor(Update.class).readValue(req);

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .body(handleRequest(update, context))
                    .header("Content-Type", "application/json")
                    .build();
        } catch (Exception e) {
            context.getLogger().info(e.toString() + Arrays.toString(e.getStackTrace()));
            return request
                    .createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Check request and try again")
                    .build();
        }
    }
}