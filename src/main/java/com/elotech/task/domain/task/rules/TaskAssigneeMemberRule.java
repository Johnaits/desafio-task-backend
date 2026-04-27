package com.elotech.task.domain.task.rules;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.task.Task;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TaskAssigneeMemberRule implements TaskRule {
    @Override
    public void validate(TaskRequestDTO data, Task existingTask, User assignee, User userLogged, Project project) {
        if(assignee == null){
            return;
        }

        if(assigneeIsOwner(project, assignee)){
            return;
        }

        if(assigneeIsMember(project.getMembers(), assignee)){
            return;
        }

        throw new IllegalArgumentException("É necessário que o responsável da tarefa seja membro do projeto");
    }

    private boolean assigneeIsOwner(Project project, User assignee){
        return project.getOwner().getId().equals(assignee.getId());
    }

    private boolean assigneeIsMember(Set<User> members, User assignee){
        return members.contains(assignee);
    }
}
