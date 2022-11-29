package edu.kmaooad.telegram;

import edu.kmaooad.Dispatcher;
import edu.kmaooad.model.UserRequest;
import edu.kmaooad.model.UserSession;
import edu.kmaooad.service.UserSessionService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.Arrays;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class StudentsBot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;

    private final Dispatcher dispatcher;
    private final UserSessionService userSessionService;

    public StudentsBot(TelegramConfig telegramConfig,
                       Dispatcher dispatcher,
                       UserSessionService userSessionService) {
        super(SetWebhook.builder().url(telegramConfig.getWebhookPath()).build());
        this.botPath = telegramConfig.getWebhookPath();
        this.botUsername = telegramConfig.getBotName();
        this.botToken = telegramConfig.getBotToken();
        this.dispatcher = dispatcher;
        this.userSessionService = userSessionService;
    }

    @Override
    //Handler for simple message
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String textFromUser = update.getMessage().getText();

            Long userId = update.getMessage().getFrom().getId();
            String userFirstName = update.getMessage().getFrom().getFirstName();

            log.info("[{}, {}]: {}", userId, userFirstName, textFromUser);

            Long chatId = update.getMessage().getChatId();
            UserSession session = userSessionService.getSession(chatId);

            UserRequest userRequest = UserRequest
                    .builder()
                    .update(update)
                    .userSession(session)
                    .chatId(chatId)
                    .build();

            BotApiMethod<?> dispatched = dispatcher.dispatch(userRequest).getResult();

            return dispatched;
        } else{
            log.warn("Unexpected update from user");
            return null;
        }
    }
}
