package com.elotech.task.domain.project;

import com.elotech.task.domain.project.dto.ProjectRequestDTO;
import com.elotech.task.domain.user.User;
import com.elotech.task.domain.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public Page<Project> findAll(User owner, Pageable pageable){
        return this.projectRepository.findAllByOwner(owner, pageable);
    }

    public Project findById(Long id, User owner){
        return this.projectRepository.findByIdAndOwner(id, owner).orElseThrow(EntityNotFoundException::new);
    }

    public Project create(ProjectRequestDTO data, User userLogged){
        User owner = userLogged;
        Project newProject = new Project(
                data.name(),
                data.description(),
                owner
        );

        return this.projectRepository.save(newProject);

    }
}
