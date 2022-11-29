package edu.kmaooad.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "group")
public class Group {
    @Id
    String id;

    String name;
    Integer grade;
    Integer year;

    List<String> activities = new ArrayList<>();
    List<String> studentIds = new ArrayList<>();
    // айді студентів
}
