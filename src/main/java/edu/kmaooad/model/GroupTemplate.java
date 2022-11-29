package edu.kmaooad.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "group_template")
public class GroupTemplate {
    @Id
    String id;

    Integer grade;
    Integer year;
    String name;
}
