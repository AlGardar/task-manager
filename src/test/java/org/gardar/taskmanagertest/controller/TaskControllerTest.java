package org.gardar.taskmanagertest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gardar.taskmanagertest.dto.TaskRequest;
import org.gardar.taskmanagertest.dto.TaskResponse;
import org.gardar.taskmanagertest.entity.Task;
import org.gardar.taskmanagertest.exception.ResourceNotFoundException;
import org.gardar.taskmanagertest.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private TaskService taskService;

    @Test
    void findAll_returnsJsonArray() throws Exception {
        TaskResponse response = new TaskResponse(
                1L, "Test task", null, Task.Status.TODO, OffsetDateTime.now()
        );
        when(taskService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test task"))
                .andExpect(jsonPath("$[0].status").value("TODO"));
    }

    @Test
    void create_withValidRequest_returns201() throws Exception {
        TaskRequest request = new TaskRequest("New task", null, null);
        TaskResponse response = new TaskResponse(
                1L, "New task", null, Task.Status.TODO, OffsetDateTime.now()
        );
        when(taskService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New task"));
    }

    @Test
    void create_withBlankTitle_returns400() throws Exception {
        TaskRequest request = new TaskRequest("", null, null);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.title").value("Title is required"));
    }

    @Test
    void update_whenNotFound_returns404() throws Exception {
        TaskRequest request = new TaskRequest("Title", null, null);
        when(taskService.update(any(), any()))
                .thenThrow(new ResourceNotFoundException("Task not found: 999"));

        mockMvc.perform(put("/api/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Task not found: 999"));
    }
}
