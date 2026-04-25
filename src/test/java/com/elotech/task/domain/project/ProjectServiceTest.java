package com.elotech.task.domain.project;

import com.elotech.task.domain.project.dto.ProjectRequestDTO;
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

    @InjectMocks
    private ProjectService projectService;

    @Test
    @DisplayName("Deve criar um projeto com sucesso associado ao usuario logado")
    void mustCreateProject(){
        User owner = getLoggedUserFake();
        String[] params = {
                "Projeto de teste",
                "Projeto criado apenas para teste interno"
        };
        Project project = new Project();
        project.setId(1L);
        project.setName(params[0]);
        project.setDescription(params[1]);
        project.setOwner(owner);
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
        when(projectRepository.findByIdAndOwner(ID_LOGGED_USER_FAKE, otherUser)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            projectService.findById(ID_LOGGED_USER_FAKE, otherUser);
        });
    }

    private User getLoggedUserFake(){
        User user = new User();
        user.setId(ID_LOGGED_USER_FAKE);
        return user;
    }
}
