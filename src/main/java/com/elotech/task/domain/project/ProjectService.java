package com.elotech.task.domain.project;

import com.elotech.task.domain.project.dto.ProjectMembersRequestDTO;
import com.elotech.task.domain.project.dto.ProjectRequestDTO;
import com.elotech.task.domain.project.exception.ExcludeProjectWithTasksException;
import com.elotech.task.domain.task.TaskRepository;
import com.elotech.task.domain.user.User;
import com.elotech.task.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            UserRepository userRepository,
            TaskRepository taskRepository
    ){
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public Page<Project> findAll(User owner, Pageable pageable){
        return this.projectRepository.findAllProjectsByOwnerOrByMember(owner, pageable);
    }

    public Project findById(Long id, User owner){
        return this.projectRepository.findByIdAndOwnerOrByMember(id, owner).orElseThrow(EntityNotFoundException::new);
    }

    public Project getProjectIfUserIsOwner(Long id, User loggedUser){
        return this.projectRepository.findByIdAndOwner(id, loggedUser).orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com este responsável"));
    }

    public Project create(ProjectRequestDTO data, User loggedUser){
        User owner = loggedUser;
        Project newProject = new Project(
                data.name(),
                data.description(),
                owner
        );

        return this.projectRepository.save(newProject);
    }

    public Project update(Long id, ProjectRequestDTO data, User loggedUser){
        Project project = getProjectIfUserIsOwner(id, loggedUser);
        project.setName(data.name());
        project.setDescription(data.description());
        return this.projectRepository.save(project);
    }

    public void delete(Long id, User loggedUser){
        Project project = getProjectIfUserIsOwner(id, loggedUser);
        Long amountTasks = this.taskRepository.countByProjectId(project.getId());
        if(amountTasks > 0L){
            throw new ExcludeProjectWithTasksException("Esse projeto possui tarefas atreladas, portanto não pode ser excluído");
        }
        this.projectRepository.delete(project);
    }

    @Transactional
    public Project addMembers(Long id, ProjectMembersRequestDTO data, User loggedUser){
        Project project = getProjectIfUserIsOwner(id, loggedUser);

        List<User> usersToAdd = this.userRepository.findAllById(data.userIds());

        if(usersToAdd.size() != data.userIds().size()){
            throw new EntityNotFoundException("Membros informados não encontrados");
        }

        project.getMembers().addAll(usersToAdd);
        return project;
    }

    @Transactional
    public Project removeMembers(Long id, List<Long> userIds, User loggedUser){
        Project project = getProjectIfUserIsOwner(id, loggedUser);

        if(project.getMembers().size() == 0){
            throw new EntityNotFoundException("Projeto não possui membros");
        }

        List<User> usersToRemove = this.userRepository.findAllById(userIds);

        if(usersToRemove.size() != userIds.size()){
            throw new EntityNotFoundException("Membros informados não encontrados");
        }

        project.getMembers().removeAll(usersToRemove);
        return project;

    }
}
