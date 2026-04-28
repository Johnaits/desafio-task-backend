package com.elotech.task.domain.task;

import com.elotech.task.domain.task.dto.TaskReportDTO;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t " +
            "LEFT JOIN t.project p " +
            "WHERE t.id = :id AND (p.owner = :user OR :user MEMBER OF p.members)")
    Optional<Task> findByIdAndOwnerOrMember(Long id, User user);

    Long countByAssigneeAndStatus(User assignee, TaskStatusEnum status);

    Long countByProjectId(Long projectId);

    @Query("SELECT new com.elotech.task.domain.task.dto.TaskReportDTO(t.status, t.priority, COUNT(t))" +
            "FROM Task t WHERE t.project.id = :idProject GROUP BY t.status, t.priority")
    List<TaskReportDTO> getTaskReportByProject(Long idProject);

}
