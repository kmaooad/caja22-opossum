package edu.kmaooad;

import edu.kmaooad.repositories.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DBSampleTest {

    @Autowired
    GroupRepository groupRepository;

    @Test
    public void test() {
        System.out.println(groupRepository.findAll());
    }
}
