package com.elotech.task.domain.project.dto;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.user.dto.UserResponseDTO; // Assumindo que você tenha um DTO para User

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record ProjectResponseDTO(
        Long id,
        String name,
        String description,
        UserResponseDTO owner,
        Set<UserResponseDTO> members,
        Instant createdAt
) {

    public ProjectResponseDTO(Project project) {
        this(
                project.getId(),
                project.getName(),
                project.getDescription(),
                new UserResponseDTO(project.getOwner()),
                project.getMembers().stream()
                        .map(UserResponseDTO::new)
                        .collect(Collectors.toSet()),
                project.getCreatedAt()
        );
    }
}