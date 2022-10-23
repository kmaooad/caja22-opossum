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

    @Autowired

    @Test
    public void testTelegramWebhook() throws Exception {
        BotUpdate botUpdateTest = BotUpdate.builder()
                .authorId("authorId1").username("AuthorUserName1").messageId("messageId1")
                .date(new Date().toString())
                .firstName("AuthorFirstName1").lastName("AuthorLastName1")
                .text("someText1").languageCode("EN")
                .build();

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
