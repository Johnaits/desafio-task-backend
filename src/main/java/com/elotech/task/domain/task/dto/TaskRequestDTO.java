package com.elotech.task.domain.task.dto;

import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskRequestDTO(
        @NotNull(message = "A tarefa deve pertencer a um projeto")
        Long idProject,

        @NotBlank(message = "A tarefa deve ter um título")
        String title,

        @NotBlank(message = "A tarefa deve ter uma descrição")
        String description,

        @NotNull(message = "A tarefa deve ter um status")
        TaskStatusEnum status,

        @NotNull(message = "A tarefa deve ter uma prioridade")
        TaskPriorityEnum priority,

        Long idAssignee,

        @NotNull(message = "A tarefa deve ter uma data limite")
        @FutureOrPresent(message = "A data limite não pode estar no passado")
        LocalDate deadline
) {
}
