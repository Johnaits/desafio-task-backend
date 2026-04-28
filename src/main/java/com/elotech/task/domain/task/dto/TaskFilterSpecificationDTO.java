package com.elotech.task.domain.task.dto;

import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TaskFilterSpecificationDTO(
        TaskStatusEnum status,
        TaskPriorityEnum priority,
        Long idAssignee,
        String query,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startCreatedAt,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endCreatedAt,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDeadline,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDeadline
) {}
