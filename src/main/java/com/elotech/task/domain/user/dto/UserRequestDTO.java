package com.elotech.task.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank(message = "Nome deve estar preenchido")
        String name,

        @NotBlank(message = "E-mail deve estar preenchido")
        String email,

        @NotBlank(message = "Deve existir uma senha")
        String password
) {
}
