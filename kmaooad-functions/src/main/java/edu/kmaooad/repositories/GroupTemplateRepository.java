package edu.kmaooad.repositories;

import edu.kmaooad.model.Group;
import edu.kmaooad.model.GroupTemplate;
import edu.kmaooad.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupTemplateRepository extends MongoRepository<GroupTemplate, String> {
    GroupTemplate  findByName(String name);
    GroupTemplate  findByYear(String name);
    GroupTemplate  findByGrade(String name);
}
