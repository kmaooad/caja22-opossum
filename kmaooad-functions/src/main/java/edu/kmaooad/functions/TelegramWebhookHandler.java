package edu.kmaooad.functions;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import edu.kmaooad.DTO.BotUpdate;
import edu.kmaooad.DTO.BotUpdateResult;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class TelegramWebhookHandler extends FunctionInvoker<BotUpdate, BotUpdateResult> {

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

        BotUpdate upd = new BotUpdate();

        upd.setMessageId("message10");

        return request
                .createResponseBuilder(HttpStatus.OK)
                .body(handleRequest(upd, context))
                .header("Content-Type", "application/json")
                .build();
    }
}