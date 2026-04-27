package com.elotech.task.domain.task;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.task.rules.TaskWipLimitRule;
import com.elotech.task.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskWipLimitRuleTest {

    private static final Long ID_LOGGED_USER_FAKE = 1L;

    private static final Long ID_PROJECT_FAKE = 1L;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskWipLimitRule taskWipLimitRule;

    @Test
    @DisplayName("Deve passar pela validação sem gerar IllegalArgumentException")
    void mustPassForValidation(){
        assertDoesNotThrow(() -> taskWipLimitRule.validate(getTaskRequestDTOFake(), getTaskFake(), getLoggedUserFake(), getLoggedUserFake(), getProjectFake()));
    }

    @Test
    @DisplayName("Deve passar pela validação sem gerar IllegalArgumentException, pois está atualizando a própria tarefa")
    void mustPassWhenUpdateOwnTask(){
        assertDoesNotThrow(() -> taskWipLimitRule.validate(getTaskRequestDTOFake(), getTaskFake(), getLoggedUserFake(), getLoggedUserFake(), getProjectFake()));
    }

    @Test
    @DisplayName("Deve lançar um IllegalArgumentException ao fazer a validação")
    void putExceptionOfWIPLimit(){
        when(taskRepository.countByAssigneeAndStatus(getLoggedUserFake(), TaskStatusEnum.IN_PROGRESS)).thenReturn(6L);
        assertThrows(IllegalArgumentException.class, () -> taskWipLimitRule.validate(getTaskRequestDTOFake(), null, getLoggedUserFake(), getLoggedUserFake(), getProjectFake()));
    }

    private TaskRequestDTO getTaskRequestDTOFake(){
        return new TaskRequestDTO(
                ID_PROJECT_FAKE,
                "Tarefa 1 do Projeto 1",
                "Primeira tarefa do primeiro projeto",
                TaskStatusEnum.IN_PROGRESS,
                TaskPriorityEnum.HIGH,
                getLoggedUserFake().getId(),
                Instant.now().plus(5, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toLocalDate()
        );
    }

    private Task getTaskFake(){
        return new Task(
            getProjectFake(),
        "Tarefa 1 do Projeto 1",
        "Primeira tarefa do primeiro projeto",
            TaskStatusEnum.IN_PROGRESS,
            TaskPriorityEnum.HIGH,
            getLoggedUserFake(),
            Instant.now().plus(5, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toLocalDate()
        );
    }

    private User getLoggedUserFake(){
        User user = new User();
        user.setId(ID_LOGGED_USER_FAKE);
        return user;
    }

    private Project getProjectFake(){
        return new Project("Projeto 1", "Descrição do Projeto 1", getLoggedUserFake());
    }
}
