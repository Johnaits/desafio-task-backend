package com.elotech.task.domain.task.rules;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.task.Task;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.user.User;
import org.springframework.stereotype.Component;


@Component
public class TaskChangeToDoneRule implements TaskRule{

    @Override
    public void validate(TaskRequestDTO data, Task existingTask, User assignee, User userLogged, Project project) {
        if(taskNotWillBeDone(data)){
            return;
        }

        if(taskIsCriticalPriorityAndUserLoggedIsNotOwner(data, userLogged, project)){
            throw new IllegalArgumentException("Tarefa com prioridade %s só pode ser mudada para %s pelo responsável do projeto"
                    .formatted(TaskPriorityEnum.CRITICAL.getDescription(), TaskStatusEnum.DONE.getDescription()));
        }
    }

    private boolean taskNotWillBeDone(TaskRequestDTO data){
        return !(data.status().equals(TaskStatusEnum.DONE));
    }

    private boolean taskIsCriticalPriorityAndUserLoggedIsNotOwner(TaskRequestDTO data, User userLogged, Project project){
        if(!(data.priority().equals(TaskPriorityEnum.CRITICAL))){
            return false;
        }
        if(!(data.status().equals(TaskStatusEnum.DONE))){
            return false;
        }
        if(project.getOwner().getId().equals(userLogged.getId())){
            return false;
        }
        return true;
    }
}
