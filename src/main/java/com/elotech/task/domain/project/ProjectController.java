package com.elotech.task.domain.project;

import com.elotech.task.domain.project.dto.ProjectMembersRequestDTO;
import com.elotech.task.domain.project.dto.ProjectRequestDTO;
import com.elotech.task.domain.project.dto.ProjectResponseDTO;
import com.elotech.task.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/projects")
@PreAuthorize("hasRole('ADMIN')")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @GetMapping()
    public Page<ProjectResponseDTO> get(
            @AuthenticationPrincipal User loggedUser,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ){
        return this.projectService.findAll(loggedUser, pageable).map(ProjectResponseDTO::new);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
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

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(
        @PathVariable Long id,
        @RequestBody @Valid ProjectRequestDTO data,
        @AuthenticationPrincipal User loggedUser
    ){
        Project project = this.projectService.update(id, data, loggedUser);
        return ResponseEntity.ok(new ProjectResponseDTO(project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User loggedUser
    ){
        this.projectService.delete(id, loggedUser);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idProject}/members")
    public ResponseEntity<ProjectResponseDTO> createMembers(
            @PathVariable Long idProject,
            @RequestBody @Valid ProjectMembersRequestDTO data,
            @AuthenticationPrincipal User loggedUser
    ){
        Project project = this.projectService.addMembers(idProject, data, loggedUser);
        return ResponseEntity.ok(new ProjectResponseDTO(project));
    }

    @DeleteMapping("/{idProject}/members")
    public ResponseEntity<ProjectResponseDTO> deleteMembers(
        @PathVariable Long idProject,
        @RequestParam List<Long> userIds,
        @AuthenticationPrincipal User loggedUser
    ){
        Project project = this.projectService.removeMembers(idProject, userIds, loggedUser);
        return ResponseEntity.ok(new ProjectResponseDTO(project));
    }
}
