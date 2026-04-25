package com.elotech.task.domain.project.dto;

import com.elotech.task.domain.project.Project;

public record ProjectResponseDTO(
        Long id,
        String name,
        String description
) {

    public ProjectResponseDTO(Project project){
        this(project.getId(), project.getName(), project.getDescription());
    }
}
