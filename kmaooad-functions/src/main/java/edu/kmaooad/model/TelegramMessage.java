/**
 * Data class for telegram message
 */
package edu.kmaooad.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "botInputMessages")
@Data
public class TelegramMessage {
    @Id
    private String messageId;

    private String authorId;
    private String firstName;
    private String lastName;
    private String username;
    private String languageCode;
    private String date;
    private String text;

}

