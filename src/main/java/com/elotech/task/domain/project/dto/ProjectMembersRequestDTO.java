package com.elotech.task.domain.project.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record ProjectMembersRequestDTO(
        @NotEmpty(message = "É necessário definir membros para o projeto")
        Set<Long> userIds
) {
}
