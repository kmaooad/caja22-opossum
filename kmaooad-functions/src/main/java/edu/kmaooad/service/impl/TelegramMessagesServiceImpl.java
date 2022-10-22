package edu.kmaooad.service.impl;

import edu.kmaooad.model.TelegramMessage;
import edu.kmaooad.repositories.TelegramMessagesRepository;
import edu.kmaooad.service.TelegramMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramMessagesServiceImpl implements TelegramMessagesService {

    @Autowired
    private TelegramMessagesRepository telegramMessagesRepository;

    @Override
    public TelegramMessage addMessage(TelegramMessage message) {
        return telegramMessagesRepository.save(message);
    }
}
