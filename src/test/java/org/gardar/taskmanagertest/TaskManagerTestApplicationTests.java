package org.gardar.taskmanagertest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfig.class)
class TaskManagerTestApplicationTests {

    @Test
    void contextLoads() {
    }

}
