package edu.kmaooad.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Component
public class StudentsBotSender extends DefaultAbsSender {
    private final TelegramConfig telegramConfig;

    protected StudentsBotSender(TelegramConfig telegramConfig){
        super(new DefaultBotOptions());
        this.telegramConfig = telegramConfig;
    }


    @Override
    public String getBotToken() {
        return telegramConfig.getBotToken();
    }
}
