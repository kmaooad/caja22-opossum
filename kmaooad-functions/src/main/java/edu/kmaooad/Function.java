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
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
                WriteTelegramJSONToDB(telegramHook);
                String messageID = jsonObject.getAsJsonObject("message").get("message_id").getAsString();
                System.out.println(messageID);
                return request.createResponseBuilder(HttpStatus.OK).body(messageID).build();
            } catch (Exception e) {
                e.printStackTrace();
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please check request and try again").build();
            }
        }
    }

    /**
     * Write telegram json response to db
     */
    public void WriteTelegramJSONToDB(String response) {
        String uri = "mongodb+srv://Opossum:Sh56MZnkL00DYYI7@opossum.egv5fqu.mongodb.net/?retryWrites=true&w=majority";

        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("opossum_bot");
        MongoCollection<org.bson.json.JsonObject> collection = database.getCollection("json_full_response", org.bson.json.JsonObject.class);
        collection.insertOne(new org.bson.json.JsonObject(response));
    }

}
