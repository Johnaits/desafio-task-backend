package com.elotech.task.domain.project.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequestDTO(
        @NotBlank(message = "O projeto deve ter um nome")
        String name,
        String description
) {


}
