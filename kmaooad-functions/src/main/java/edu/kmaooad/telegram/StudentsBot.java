package edu.kmaooad.telegram;

import edu.kmaooad.constants.bot.BotMessageEnum;
import edu.kmaooad.functions.TelegramWebhook;
import edu.kmaooad.telegram.handlers.CallbackQueryHandler;
import edu.kmaooad.telegram.handlers.MessageHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentsBot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;

    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;

    public StudentsBot(SetWebhook setWebhook,
                       MessageHandler messageHandler,
                       CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    //Handler for simple message
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return sendMessage((SendMessage) handleUpdate(update));
        } catch (IllegalArgumentException e) {
            return sendMessage(update.getMessage().getChatId().toString(),
                    BotMessageEnum.EXCEPTION_ILLEGAL_MESSAGE.getMessage());
        } catch (Exception e) {
            return sendMessage(update.getMessage().getChatId().toString(),
                    BotMessageEnum.EXCEPTION_UNKNOWN.getMessage());
        }
    }

    //Handler for inline buttons in bot
    private BotApiMethod<?> handleUpdate(Update update) throws IOException, TelegramApiException {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            Message message = update.getMessage();

            if (message != null) {
                return messageHandler.answerMessage(update.getMessage());
            }
        }
        return null;
    }

    /**
     * Send message through telegram api and return response
     */

    public BotApiMethod<?> sendMessage(Long chatId, String text) {
        return sendMessage(chatId, text, null);
    }

    public BotApiMethod<?> sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage
                .builder()
                .text(text)
                .chatId(chatId.toString())
                //Other possible parse modes: MARKDOWNV2, MARKDOWN, which allows to make text bold, and all other things
                .parseMode(ParseMode.HTML)
                .replyMarkup(replyKeyboard)
                .build();
        try {
            execute(sendMessage);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return sendMessage;
    }

    public BotApiMethod<?> sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return sendMessage;
    }

}
