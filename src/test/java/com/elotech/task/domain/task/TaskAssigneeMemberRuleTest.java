package com.elotech.task.domain.task;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.task.rules.TaskAssigneeMemberRule;
import com.elotech.task.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskAssigneeMemberRuleTest {

    private final TaskAssigneeMemberRule rule = new TaskAssigneeMemberRule();

    @Test
    @DisplayName("Deve passar na validação quando a tarefa não tiver responsável (assignee nulo)")
    void mustPassWhenAssigneeIsNull() {
        Project project = getProjectFake(getUserFake(1L));
        assertDoesNotThrow(() -> rule.validate(getDtoFake(), null, null, getUserFake(1L), project));
    }

    @Test
    @DisplayName("Deve passar na validação quando o responsável for o próprio DONO do projeto")
    void mustPassWhenAssigneeIsOwner() {
        User owner = getUserFake(1L);
        Project project = getProjectFake(owner);
        assertDoesNotThrow(() -> rule.validate(getDtoFake(), null, owner, owner, project));
    }

    @Test
    @DisplayName("Deve passar na validação quando o responsável for um MEMBRO do projeto")
    void mustPassWhenAssigneeIsMember() {
        User member = getUserFake(2L);
        Project project = getProjectFake(getUserFake(1L));
        project.getMembers().add(member); // Adicionando o usuário 2 à lista de membros

        assertDoesNotThrow(() -> rule.validate(getDtoFake(), null, member, getUserFake(1L), project));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o responsável NÃO FOR membro e nem dono do projeto")
    void mustThrowExceptionWhenAssigneeIsOutsider() {
        User outsider = getUserFake(99L); // O intruso que não pertence ao projeto
        Project project = getProjectFake(getUserFake(1L));
        project.getMembers().add(getUserFake(2L)); // Um membro comum

        assertThrows(IllegalArgumentException.class, () -> rule.validate(getDtoFake(), null, outsider, getUserFake(1L), project));
    }

    private TaskRequestDTO getDtoFake() {
        return new TaskRequestDTO(1L, "Titulo", "Desc", TaskStatusEnum.TODO, TaskPriorityEnum.LOW, null, LocalDate.now());
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