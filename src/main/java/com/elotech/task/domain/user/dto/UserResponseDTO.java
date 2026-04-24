package com.elotech.task.domain.user.dto;

import com.elotech.task.domain.user.User;

public record UserResponseDTO(Long id, String name, String email) {

    public UserResponseDTO(User user){
        this(user.getId(), user.getName(), user.getEmail());
    }
}
