package edu.kmaooad.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "activity")
public class Activity {
    @Id
    String id;
    String name;
    LocalDate startDate;
    LocalDate endDate;
    String status;
}
