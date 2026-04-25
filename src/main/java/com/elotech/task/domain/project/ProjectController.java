package com.elotech.task.domain.project;

import com.elotech.task.domain.project.dto.ProjectRequestDTO;
import com.elotech.task.domain.project.dto.ProjectResponseDTO;
import com.elotech.task.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping()
    public Page<ProjectResponseDTO> get(
            @AuthenticationPrincipal User loggedUser,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ){
        return this.projectService.findAll(loggedUser, pageable).map(ProjectResponseDTO::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User loggedUser
    ){
        Project project = this.projectService.findById(id, loggedUser);
        return ResponseEntity.ok(new ProjectResponseDTO(project));
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(
            @RequestBody @Valid ProjectRequestDTO data,
            @AuthenticationPrincipal User loggedUser,
            UriComponentsBuilder uriBuilder
    ){
        Project newProject = this.projectService.create(data, loggedUser);

        var uri = uriBuilder.path("/projects/{id}").buildAndExpand(newProject.getId()).toUri();

        return ResponseEntity.created(uri).body(new ProjectResponseDTO(newProject));
    }
}
