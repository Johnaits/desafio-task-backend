package com.elotech.task.domain.user.dto;

import com.elotech.task.domain.user.UserRolesEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotBlank(message = "Nome deve estar preenchido")
        String name,

        @NotBlank(message = "E-mail deve estar preenchido")
        String email,

        @NotBlank(message = "Deve existir uma senha")
        String password,

        @NotNull(message = "O perfil do usuário é obrigatório")
        UserRolesEnum role
) {
}
