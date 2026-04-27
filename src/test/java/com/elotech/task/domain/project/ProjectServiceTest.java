package com.elotech.task.domain.project;

import com.elotech.task.domain.project.dto.ProjectRequestDTO;
import com.elotech.task.domain.project.exception.ExcludeProjectWithTasksException;
import com.elotech.task.domain.task.TaskRepository;
import com.elotech.task.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    private static final Long ID_LOGGED_USER_FAKE = 1L;

    @Mock
    private ProjectRepository projectRepository;

    @Mock private TaskRepository taskRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    @DisplayName("Deve criar um projeto com sucesso associado ao usuario logado")
    void mustCreateProject(){
        User owner = getLoggedUserFake();
        Project project = getProjectFake();
        project.setId(1L);
        String[] params = {
                "Projeto de teste",
                "Projeto criado apenas para teste interno"
        };
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        ProjectRequestDTO requestDTO = new ProjectRequestDTO(params[0], params[1]);


        Project projectReturned = this.projectService.create(requestDTO, owner);

        assertNotNull(projectReturned);
        assertEquals(1L, projectReturned.getId());
        assertEquals(params[0], projectReturned.getName());
        assertEquals(params[1], projectReturned.getDescription());
        assertEquals(ID_LOGGED_USER_FAKE, projectReturned.getOwner().getId());

        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Deve lancar EntityNotFoundException quando buscar projeto de outro dono")
    void putExceptionWhenNotOwnerOfProject(){
        User otherUser = new User();
        otherUser.setId(99L);

        // Ao buscar o outro usuario com o id do usuario logado, retorna vazio
        when(projectRepository.findByIdAndOwnerOrByMember(ID_LOGGED_USER_FAKE, otherUser)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            projectService.findById(ID_LOGGED_USER_FAKE, otherUser);
        });
    }

    @Test
    @DisplayName("Deve alterar a descrição e nome de um projeto")
    void mustUpdateProject(){
        User owner = getLoggedUserFake();
        Long id = 2L;

        Project project = getProjectFake();
        project.setId(id);

        String[] params = {
                "Projeto de teste 2",
                "Projeto alterado apenas para teste interno"
        };
        project.setName(params[0]);
        project.setDescription(params[1]);
        ProjectRequestDTO requestDTO = new ProjectRequestDTO(params[0], params[1]);

        when(projectRepository.findByIdAndOwner(id, owner)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project projectReturned = this.projectService.update(id, requestDTO, owner);
        assertNotNull(projectReturned);
        assertEquals(id, projectReturned.getId());
        assertEquals(params[0], projectReturned.getName());
        assertEquals(params[1], projectReturned.getDescription());
        assertEquals(ID_LOGGED_USER_FAKE, projectReturned.getOwner().getId());

        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Deve lancar EntityNotFoundException quando alterar projeto de outro dono")
    void putExceptionWhenUpdateProjectOfOtherOwner(){
        Project project = getProjectFake();
        project.setId(1L);
        User otherUser = getLoggedUserFake(2L);

        // Ao buscar o projeto de outro usuario, retorna vazio
        when(projectRepository.findByIdAndOwner(project.getId(), otherUser)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            this.projectService.update(project.getId(), new ProjectRequestDTO(project.getName(), project.getDescription()), otherUser);
        });

        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve fazer a exclusão de um projeto")
    void mustExcludeProject(){
        Project project = getProjectFake();
        Long id = 1L;
        project.setId(id);
        User owner = getLoggedUserFake();

        // Encontra o projeto
        when(this.projectRepository.findByIdAndOwner(id, owner)).thenReturn(Optional.of(project));

        // Não encontra tarefa
        when(this.taskRepository.countByProjectId(id)).thenReturn(0L);

        assertDoesNotThrow(() -> this.projectService.delete(id, owner));

        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando não existir o projeto")
    void putExceptionExcludeWhenNotExistProject(){
        Long idNotExistant = 99L;
        User owner = getLoggedUserFake();

        when(projectRepository.findByIdAndOwner(idNotExistant, owner)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            this.projectService.delete(idNotExistant, owner);
        });
    }

    @Test
    @DisplayName("Deve lançar ExcludeProjectWithTasksException quando o projeto possuir tarefas atreladas")
    void putExceptionExcludeWhenProjectHaveChildTasks(){
        User owner = getLoggedUserFake();
        Long id = 1L;
        Project project = getProjectFake();
        project.setId(id);

        // Encontra o projeto
        when(projectRepository.findByIdAndOwner(id, owner)).thenReturn(Optional.of(project));

        // Encontra 3 tarefas
        when(taskRepository.countByProjectId(id)).thenReturn(3L);

        assertThrows(ExcludeProjectWithTasksException.class, () -> {
            this.projectService.delete(id, owner);
        });
    }

    private User getLoggedUserFake(){
        User user = new User();
        user.setId(ID_LOGGED_USER_FAKE);
        return user;
    }

    private User getLoggedUserFake(Long id){
        User user = new User();
        user.setId(id);
        return user;
    }

    private Project getProjectFake(){
        return new Project("Projeto de teste", "Projeto criado apenas para teste interno", getLoggedUserFake());
    }
}
