package com.elotech.task.domain.task;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.user.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class TaskSpecifications {

    public static Specification<Task> userHasAccess(User user){
        return (root, query, cb) -> {
            Join<Task, Project> projectJoin = root.join("project");
            return cb.or(
                    cb.equal(projectJoin.get("owner"), user),
                    cb.isMember(user, projectJoin.get("members"))
            );
        };
    }

    public static Specification<Task> hasStatus(TaskStatusEnum status){
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(TaskPriorityEnum priority){
        return (root, query, cb) -> priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasAssignee(Long idAssignee){
        return (root, query, cb) -> idAssignee == null ? null : cb.equal(root.get("assignee").get("id"), idAssignee);
    }

    public static Specification<Task> haveCreatedAt(LocalDate startCreatedAt, LocalDate endCreatedAt){
        return (root, query, cb) -> {
            if(startCreatedAt == null && endCreatedAt == null) return null;

            Instant startInstant = startCreatedAt != null ?
                    startCreatedAt.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;

            Instant endInstant = endCreatedAt != null ?
                    endCreatedAt.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant() : null;

            if(startInstant != null && endInstant != null){
                return cb.between(root.get("createdAt"), startInstant, endInstant);
            }

            if(startInstant != null){
                return cb.greaterThanOrEqualTo(root.get("createdAt"), startInstant);
            }

            return cb.lessThanOrEqualTo(root.get("createdAt"), endInstant);
        };
    }

    public static Specification<Task> haveDeadline(LocalDate startDeadline, LocalDate endDeadline){
        return (root, query, cb) -> {
            if(startDeadline == null && endDeadline == null) return null;

            if(startDeadline != null && endDeadline != null){
                return cb.between(root.get("deadline"), startDeadline, endDeadline);
            }

            if(startDeadline != null){
                return cb.greaterThanOrEqualTo(root.get("deadline"), startDeadline);
            }

            return cb.lessThanOrEqualTo(root.get("deadline"), endDeadline);
        };
    }

    public static Specification<Task> searchTerm(String term){
        return (root, query, cb) -> {
            if(term == null || term.isBlank()) return null;
            String likeTerm = "%" + term.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), likeTerm),
                    cb.like(cb.lower(root.get("description")), likeTerm)
            );
        };
    }
}
