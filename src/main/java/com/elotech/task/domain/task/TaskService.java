package com.elotech.task.domain.task;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.project.ProjectService;
import com.elotech.task.domain.task.dto.TaskFilterSpecificationDTO;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.task.rules.TaskRule;
import com.elotech.task.domain.user.User;
import com.elotech.task.domain.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskHistoryRepository taskHistoryRepository;

    private final ProjectService projectService;

    private final UserService userService;

    private final List<TaskRule> validators;

    public TaskService(
            TaskRepository taskRepository,
            TaskHistoryRepository taskHistoryRepository,
            ProjectService projectService,
            UserService userService,
            List<TaskRule> validators
    ){
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
        this.projectService = projectService;
        this.userService = userService;
        this.validators = validators;
    }

    @Transactional
    public Task create(TaskRequestDTO data, User userLogged){
        Project project = this.projectService.findById(data.idProject(), userLogged);

        User assignee = getAssigneeIfExists(data.idAssignee());

        validators.forEach(rule -> rule.validate(data, null, assignee, userLogged, project));

        Task newTask = new Task(
                project,
                data.title(),
                data.description(),
                data.status(),
                data.priority(),
                assignee,
                data.deadline()
        );
        this.taskRepository.save(newTask);
        saveHistory(newTask, userLogged);
        return newTask;
    }

    @Transactional
    public Task update(Long id, TaskRequestDTO data, User userLogged){
        Project project = this.projectService.findById(data.idProject(), userLogged);

        User assignee = getAssigneeIfExists(data.idAssignee());

        Task task = findById(id, userLogged);

        validators.forEach(rule -> rule.validate(data, task, assignee, userLogged, project));

        task.setProject(project);
        task.setTitle(data.title());
        task.setDescription(data.description());
        task.setStatus(data.status());
        task.setPriority(data.priority());
        task.setAssignee(assignee);
        task.setDeadline(data.deadline());
        this.taskRepository.save(task);
        saveHistory(task, userLogged);
        return task;
    }

    public void delete(Long id, User userLogged){
        Task task = findById(id, userLogged);
        this.taskRepository.delete(task);
    }

    public Page<Task> findAll(
            User userLogged,
            TaskFilterSpecificationDTO filterSpecification,
            Pageable pageable
    ){
        Specification<Task> spec = TaskSpecifications.userHasAccess(userLogged);

        spec = spec.and(TaskSpecifications.hasStatus(filterSpecification.status()))
                .and(TaskSpecifications.hasPriority(filterSpecification.priority()))
                .and(TaskSpecifications.hasAssignee(filterSpecification.idAssignee()))
                .and(TaskSpecifications.haveCreatedAt(filterSpecification.startCreatedAt(), filterSpecification.endCreatedAt()))
                .and(TaskSpecifications.haveDeadline(filterSpecification.startDeadline(), filterSpecification.endDeadline()))
                .and(TaskSpecifications.searchTerm(filterSpecification.query()));

        return this.taskRepository.findAll(spec, pageable);
    }

    public Task findById(Long id, User userLogged){
        return this.taskRepository.findByIdAndOwnerOrMember(id, userLogged).orElseThrow( () -> new EntityNotFoundException("Tarefa de ID " + id + " não encontrada para este responsável"));
    }

    private User getAssigneeIfExists(Long idAssignee) {
        if (idAssignee == null) {
            return null;
        }
        return this.userService.findById(idAssignee);
    }

    private void saveHistory(Task task, User userLogged){
        TaskHistory newTaskHistory = new TaskHistory(
                task,
                task.getStatus(),
                task.getAssignee(),
                userLogged
        );
        this.taskHistoryRepository.save(newTaskHistory);
    }
}
