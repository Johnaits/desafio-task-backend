package com.elotech.task.config.seeder;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.project.ProjectRepository;
import com.elotech.task.domain.task.TaskRepository;
import com.elotech.task.domain.task.TaskService;
import com.elotech.task.domain.task.dto.TaskRequestDTO;
import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.user.User;
import com.elotech.task.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
@ConditionalOnProperty(name = "app.seeder.enabled", havingValue = "true")
@Order(3)
public class TaskSeeder implements CommandLineRunner {

    private final TaskService taskService;

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    public TaskSeeder(TaskService taskService, TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.taskRepository.count() > 0) {
            System.out.println("Tarefas já existentes no banco. TaskSeeder pulada.");
            return;
        }

        String defaultEmail = "testeApiApplication@gmail.com";
        User loggedUser = this.userRepository.findByEmail(defaultEmail)
                .orElseThrow(() -> new RuntimeException("Usuário default não encontrado."));

        Project project = this.projectRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum projeto encontrado para vincular as tarefas."));

        // Criando Tarefa 1 (TODO)
        this.taskService.create(new TaskRequestDTO(
                project.getId(),
                "Modelagem do Banco de Dados",
                "Criar o diagrama DER e os scripts de inicialização",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.HIGH,
                loggedUser.getId(),
                Instant.now().plus(5, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toLocalDate()
        ), loggedUser);

        // Criando Tarefa 2 (IN_PROGRESS)
        this.taskService.create(new TaskRequestDTO(
                project.getId(),
                "Implementar Autenticação",
                "Configurar o Spring Security e gerar o token JWT",
                TaskStatusEnum.IN_PROGRESS,
                TaskPriorityEnum.CRITICAL,
                loggedUser.getId(),
                Instant.now().plus(2, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toLocalDate()
        ), loggedUser);

        System.out.println("Tarefas iniciais criadas e vinculadas ao projeto com sucesso!");
    }
}