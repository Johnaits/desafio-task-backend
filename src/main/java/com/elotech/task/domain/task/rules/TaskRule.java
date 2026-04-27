package com.elotech.task.domain.task.rules;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.task.Task;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.user.User;

public interface TaskRule {

    void validate(
            TaskRequestDTO data,
            Task existingTask,
            User assignee,
            User userLogged,
            Project project
    );

}
