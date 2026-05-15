package org.gardar.taskmanagertest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.gardar.taskmanagertest.entity.Task;

public record TaskRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must be 100 characters or less")
        String title,

        @Size(max = 1000, message = "Description must be 1000 characters or less")
        String description,

        Task.Status status
) {
}
