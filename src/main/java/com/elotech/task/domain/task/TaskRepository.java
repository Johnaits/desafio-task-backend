package com.elotech.task.domain.task;

import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t " +
            "LEFT JOIN t.project p " +
            "WHERE t.id = :id AND (p.owner = :user OR :user MEMBER OF p.members)")
    Optional<Task> findByIdAndOwnerOrMember(Long id, User user);

    Long countByAssigneeAndStatus(User assignee, TaskStatusEnum status);

    Long countByProjectId(Long projectId);

    @Query("SELECT t FROM Task t " +
            "LEFT JOIN t.project p " +
            "WHERE (p.owner = :user OR :user MEMBER OF p.members)")
    Page<Task> findAllTasksByOwnerOrByMember(User user, Pageable pageable);
}
