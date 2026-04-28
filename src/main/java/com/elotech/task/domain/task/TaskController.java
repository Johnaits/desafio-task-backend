package com.elotech.task.domain.task;

import com.elotech.task.domain.task.dto.TaskFilterSpecificationDTO;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.task.dto.TaskResponseDTO;
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
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(
            @RequestBody @Valid TaskRequestDTO data,
            @AuthenticationPrincipal User loggedUser,
            UriComponentsBuilder uriBuilder
    ){
        Task newTask = this.taskService.create(data, loggedUser);

        var uri = uriBuilder.path("/tasks/{id}").buildAndExpand(newTask.getId()).toUri();

        return ResponseEntity.created(uri).body(new TaskResponseDTO(newTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid TaskRequestDTO data,
            @AuthenticationPrincipal User loggedUser
    ){
        Task task = this.taskService.update(id, data, loggedUser);
        return ResponseEntity.ok(new TaskResponseDTO(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User loggedUser
    ){
        this.taskService.delete(id, loggedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<TaskResponseDTO> get(
            @AuthenticationPrincipal User loggedUser,
            TaskFilterSpecificationDTO filterSpecification,
            @PageableDefault(size = 10, sort="createdAt") Pageable pageable
    ){
        return this.taskService.findAll(loggedUser, filterSpecification, pageable)
                .map(TaskResponseDTO::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User loggedUser
    ){
        Task task = this.taskService.findById(id, loggedUser);
        return ResponseEntity.ok(new TaskResponseDTO(task));
    }
}
