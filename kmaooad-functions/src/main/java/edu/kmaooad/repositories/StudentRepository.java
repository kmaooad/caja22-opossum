package edu.kmaooad.repositories;

import edu.kmaooad.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByEmail(String email);

    Optional<Student> findById(String id);
}
