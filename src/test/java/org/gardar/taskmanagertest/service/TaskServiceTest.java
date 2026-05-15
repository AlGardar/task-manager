package org.gardar.taskmanagertest.service;

import org.gardar.taskmanagertest.dto.TaskRequest;
import org.gardar.taskmanagertest.dto.TaskResponse;
import org.gardar.taskmanagertest.entity.Task;
import org.gardar.taskmanagertest.exception.ResourceNotFoundException;
import org.gardar.taskmanagertest.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void findAll_returnsMappedResponse() {
        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Test Description");
        task.setStatus(Task.Status.TODO);
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskResponse> result = taskService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Test task");
        assertThat(result.get(0).description()).isEqualTo("Test Description");
        assertThat(result.get(0).status()).isEqualTo(Task.Status.TODO);
    }

    @Test
    void create_withNoStatus_defaultsTodo() {
        TaskRequest request = new TaskRequest("New task", null, null);
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskResponse result = taskService.create(request);

        assertThat(result.status()).isEqualTo(Task.Status.TODO);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void update_whenTaskNotFound_throwsException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        TaskRequest request = new TaskRequest("Title", null, null);

        assertThatThrownBy(() -> taskService.update(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}
