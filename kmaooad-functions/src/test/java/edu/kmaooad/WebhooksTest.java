package edu.kmaooad;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.kmaooad.DTO.BotUpdate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

@Testcontainers
@RunWith(SpringRunner.class)
@FunctionalSpringBootTest
@AutoConfigureWebTestClient
public class WebhooksTest {

    @Autowired
    private WebTestClient client;


    @Test
    public void testTelegramWebhook() throws Exception {
        BotUpdate botUpdateTest = new BotUpdate();
        botUpdateTest.setAuthorId("authorId1");
        botUpdateTest.setUsername("AuthorUserName1");
        botUpdateTest.setMessageId("messageId1");
        botUpdateTest.setDate(new Date().toString());
        botUpdateTest.setFirstName("AuthorFirstName1342341");
        botUpdateTest.setLastName("AuthorLastName1");
        botUpdateTest.setText("someText1");
        botUpdateTest.setLanguageCode("EN");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        client.post().uri("/api/TelegramWebhook")
                .bodyValue(ow.writeValueAsString(botUpdateTest)).exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"success\":true,\"messageId\":\"messageId1\",\"wholeMessage\":null,\"l\":3}");

    }

    @Test
    public void testTelegramWebhookFail() {
        client.post().uri("/api/TelegramWebhook")
                .bodyValue("").exchange()
                .expectStatus().is5xxServerError();
    }

}
