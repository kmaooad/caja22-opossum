package edu.kmaooad.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BotUpdate {

    private String messageId;
    private String wholeMessage;

    /**
     * Look here for details about deserializing
     * Source: https://www.baeldung.com/jackson-nested-values
     */
    @SuppressWarnings("unchecked")
    @JsonProperty("message")
    private void unpackNested(Map<String, Object> message){
        this.messageId = message.get("message_id").toString();
    }
}