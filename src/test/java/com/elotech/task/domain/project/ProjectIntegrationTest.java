package com.elotech.task.domain.project;

import com.elotech.task.domain.project.dto.ProjectRequestDTO;
import com.elotech.task.domain.user.User;
import com.elotech.task.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve criar e salvar um projeto via API")
    void mustCreateProject() throws Exception {
        User loggedUser = createUser();
        String[] params = {
                "Projeto de teste",
                "Projeto de teste interno para validação de integração"
        };
        ProjectRequestDTO requestDTO = new ProjectRequestDTO(params[0], params[1]);

        // Converte para JSON
        String jsonRequest = objectMapper.writeValueAsString(requestDTO);

        // Aponta e inicia a requisicao
        var request = mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .with(user(loggedUser))
        );

        // Aguarda ser criado e valida o retorno
        request.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(params[0]))
                .andExpect(jsonPath("$.description").value(params[1]))
                .andExpect(jsonPath("$.owner.id").value(loggedUser.getId())
        );
    }

    private User createUser(){
        User user = new User();
        user.setName("teste");
        user.setEmail("teste_integratio@gmail.com");
        user.setPassword("123456");
        return userRepository.save(user);
    }


}
