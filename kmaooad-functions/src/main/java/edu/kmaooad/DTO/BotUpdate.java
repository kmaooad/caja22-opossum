package edu.kmaooad.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class BotUpdate {

    private String messageId;
    private String authorId;
    private String firstName;
    private String lastName;
    private String username;
    private String languageCode;

    private String date;
    private String text;

    /**
     * Look here for details about deserializing
     * Source: <a href="https://www.baeldung.com/jackson-nested-values">...</a>
     */
    @SuppressWarnings("unchecked")
    @JsonProperty("message")
    private void unpackNested(Map<String, Object> message) {
        this.messageId = message.get("message_id").toString();

        Map<String, Object> from = (Map<String, Object>) message.get("from");
        this.authorId = from.get("id").toString();
        this.firstName = from.get("first_name").toString();
        this.lastName = from.get("last_name").toString();
        this.username = from.get("username").toString();
        this.languageCode = from.get("language_code").toString();
    }
}