package org.gardar.taskmanagertest.repository;

import org.gardar.taskmanagertest.TestcontainersConfig;
import org.gardar.taskmanagertest.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfig.class)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void findByStatus_returnsOnlyMatchingTasks() {
        Task todo = new Task();
        todo.setTitle("Todo task");
        todo.setStatus(Task.Status.TODO);
        taskRepository.save(todo);

        Task inProgress = new Task();
        inProgress.setTitle("In progress task");
        inProgress.setStatus(Task.Status.IN_PROGRESS);
        taskRepository.save(inProgress);

        List<Task> result = taskRepository.findByStatus(Task.Status.TODO);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Todo task");
    }

    @Test
    void findByTitleContainingIgnoreCase_isCaseInsensitive() {
        Task task = new Task();
        task.setTitle("Learn Spring Boot");
        task.setStatus(Task.Status.TODO);
        taskRepository.save(task);

        List<Task> result =
                taskRepository.findByTitleContainingIgnoreCase("spring");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Learn Spring Boot");
    }

    @Test
    void save_persistsAllFields() {
        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Test description");
        task.setStatus(Task.Status.IN_PROGRESS);
        taskRepository.save(task);

        Task found = taskRepository.findById(task.getId()).orElseThrow();

        assertThat(found.getTitle()).isEqualTo("Test task");
        assertThat(found.getDescription()).isEqualTo("Test description");
        assertThat(found.getStatus()).isEqualTo(Task.Status.IN_PROGRESS);
        assertThat(found.getCreatedAt()).isNotNull();
    }
}
