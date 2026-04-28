package com.elotech.task.domain.task.dto;

import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;

public record TaskReportDTO(
        TaskStatusEnum status,
        TaskPriorityEnum priority,
        Long amount
) {
}
