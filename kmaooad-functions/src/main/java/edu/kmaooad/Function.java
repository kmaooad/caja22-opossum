package edu.kmaooad;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/TelegramWebhook". To invoke it using "curl" command in bash:
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

        final String telegramHook = request.getBody().orElse(null);

        if (telegramHook == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a hook date from telegram").build();
        } else {
            try {
                JsonObject jsonObject = JsonParser.parseString(telegramHook).getAsJsonObject();
                String messageID = jsonObject.getAsJsonObject("message").get("message_id").getAsString();
                System.out.println(messageID);
                return request.createResponseBuilder(HttpStatus.OK).body(messageID).build();
            } catch (Exception e) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please check request and try again").build();
            }
        }
    }

}
