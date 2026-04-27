package com.elotech.task.config.seeder;

import com.elotech.task.domain.project.ProjectRepository;
import com.elotech.task.domain.project.ProjectService;
import com.elotech.task.domain.project.dto.ProjectRequestDTO;
import com.elotech.task.domain.user.User;
import com.elotech.task.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.seeder.enabled", havingValue = "true")
@Order(2)
public class ProjectSeeder implements CommandLineRunner {

    private final ProjectService projectService;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    public ProjectSeeder(ProjectService projectService, ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.projectRepository.count() > 0) {
            System.out.println("Projetos já existentes no banco. ProjectSeeder pulada.");
            return;
        }

        String defaultEmail = UserSeeder.DEFAULT_EMAIL;
        User owner = this.userRepository.findByEmail(defaultEmail)
                .orElseThrow(() -> new RuntimeException("Usuário default não encontrado para criar projetos."));

        this.projectService.create(
                new ProjectRequestDTO("Projeto Alpha", "Sistema de gestão de tarefas internas"), owner
        );

        this.projectService.create(
                new ProjectRequestDTO("Projeto Beta", "Migração de sistema legado"), owner
        );

        System.out.println("Projetos iniciais (Alpha e Beta) criados com sucesso!");
    }
}