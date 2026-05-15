package org.gardar.taskmanagertest.dto;

import org.gardar.taskmanagertest.entity.Task;

import java.time.OffsetDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Task.Status status,
        OffsetDateTime createdAt
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }
}
