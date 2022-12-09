package edu.kmaooad.repositories;

import edu.kmaooad.model.Activity;
import edu.kmaooad.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ActivityRepository extends MongoRepository<Activity, String> {
    Optional<Activity> findByName(String name);
}
