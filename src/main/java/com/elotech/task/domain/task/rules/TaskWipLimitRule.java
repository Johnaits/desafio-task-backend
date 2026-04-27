package com.elotech.task.domain.task.rules;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.task.Task;
import com.elotech.task.domain.task.TaskRepository;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class TaskWipLimitRule implements TaskRule {

    private static final int WIP_LIMIT = 5;

    private final TaskRepository taskRepository;

    public TaskWipLimitRule(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public void validate(TaskRequestDTO data, Task existingTask, User assignee, User userLogged, Project project){
        if (taskNotInProgress(data, assignee)) {
            return;
        }

        if(updatingOwnTask(existingTask, assignee)){
            return;
        }

        Long countTask = this.taskRepository.countByAssigneeAndStatus(assignee, TaskStatusEnum.IN_PROGRESS);
        if(countTask >= WIP_LIMIT) {
            throw new IllegalArgumentException("Não pode haver mais de %s tarefas %s".formatted(WIP_LIMIT, TaskStatusEnum.IN_PROGRESS.getDescription()));
        }

    }

    private boolean taskNotInProgress(TaskRequestDTO data, User assignee){
        return assignee == null || data.status() != TaskStatusEnum.IN_PROGRESS;
    }

    private boolean updatingOwnTask(Task existingTask, User assignee){
        return existingTask != null
                && existingTask.getStatus() == TaskStatusEnum.IN_PROGRESS
                && existingTask.getAssignee() != null
                && existingTask.getAssignee().getId().equals(assignee.getId());
    }
}
