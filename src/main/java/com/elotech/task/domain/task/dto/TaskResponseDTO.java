package com.elotech.task.domain.task.dto;

import com.elotech.task.domain.task.Task;

import java.time.LocalDate;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        String status,
        String priority,
        LocalDate deadline,
        Long idProject,
        Long idAssignee
) {

    public TaskResponseDTO(Task task){

        this(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getPriority().name(),
                task.getDeadline(),
                task.getProject().getId(),
                task.getAssignee() != null ? task.getAssignee().getId() : null
        );
    }
}
