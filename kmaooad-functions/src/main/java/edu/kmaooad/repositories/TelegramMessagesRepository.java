package edu.kmaooad.repositories;

import edu.kmaooad.model.TelegramMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TelegramMessagesRepository extends MongoRepository<TelegramMessage, String> {
}
