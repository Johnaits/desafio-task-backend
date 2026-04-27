package com.elotech.task.domain.task;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.task.rules.TaskChangeToDoneRule;
import com.elotech.task.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskChangeToDoneRuleTest {

    private final TaskChangeToDoneRule rule = new TaskChangeToDoneRule();

    @Test
    @DisplayName("Deve passar na validação quando o status NÃO for DONE")
    void mustPassWhenStatusIsNotDone() {
        TaskRequestDTO dto = getDtoFake(TaskStatusEnum.IN_PROGRESS, TaskPriorityEnum.CRITICAL);
        User userLogged = getUserFake(2L); // Usuário comum (não é dono)
        Project project = getProjectFake(getUserFake(1L)); // O dono é o ID 1

        assertDoesNotThrow(() -> rule.validate(dto, null, null, userLogged, project));
    }

    @Test
    @DisplayName("Deve passar na validação quando a prioridade NÃO for CRITICAL, mesmo sendo DONE")
    void mustPassWhenPriorityIsNotCritical() {
        TaskRequestDTO dto = getDtoFake(TaskStatusEnum.DONE, TaskPriorityEnum.HIGH);
        User userLogged = getUserFake(2L);
        Project project = getProjectFake(getUserFake(1L));

        assertDoesNotThrow(() -> rule.validate(dto, null, null, userLogged, project));
    }

    @Test
    @DisplayName("Deve passar na validação quando for DONE e CRITICAL, e o usuário logado for o DONO")
    void mustPassWhenIsDoneCriticalAndUserIsOwner() {
        TaskRequestDTO dto = getDtoFake(TaskStatusEnum.DONE, TaskPriorityEnum.CRITICAL);
        User owner = getUserFake(1L);
        Project project = getProjectFake(owner);

        assertDoesNotThrow(() -> rule.validate(dto, null, null, owner, project));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando for DONE e CRITICAL, e o usuário NÃO for o DONO")
    void mustThrowExceptionWhenIsDoneCriticalAndUserIsNotOwner() {
        TaskRequestDTO dto = getDtoFake(TaskStatusEnum.DONE, TaskPriorityEnum.CRITICAL);
        User notOwner = getUserFake(2L); // O invasor tentando fechar a tarefa crítica
        Project project = getProjectFake(getUserFake(1L)); // O dono real

        assertThrows(IllegalArgumentException.class, () -> rule.validate(dto, null, null, notOwner, project));
    }

    private TaskRequestDTO getDtoFake(TaskStatusEnum status, TaskPriorityEnum priority) {
        return new TaskRequestDTO(1L, "Titulo", "Desc", status, priority, 1L, LocalDate.now());
    }

    private User getUserFake(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private Project getProjectFake(User owner) {
        Project project = new Project();
        project.setOwner(owner);
        return project;
    }
}