package com.elotech.task.domain.task;

import com.elotech.task.domain.project.Project;
import com.elotech.task.domain.task.enums.TaskPriorityEnum;
import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name="tasks")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tasks SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_project", referencedColumnName = "id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 250)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriorityEnum priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_assignee", referencedColumnName = "id")
    private User assignee;

    @Column(nullable = false)
    private LocalDate deadline;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Column(name="deleted_at")
    private Instant deletedAt;

    public Task(){

    }

    public Task(
            Project project,
            String title,
            String description,
            TaskStatusEnum status,
            TaskPriorityEnum priority,
            User assignee,
            LocalDate deadline

    ){
        this.project = project;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignee = assignee;
        this.deadline = deadline;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        if(this.status == TaskStatusEnum.DONE && status == TaskStatusEnum.TODO ){
            String message = "Status não pode sair de %s para %s";
            throw new IllegalArgumentException(message.formatted(TaskStatusEnum.DONE.getDescription(), TaskStatusEnum.TODO.getDescription()));
        }
        this.status = status;
    }

    public TaskPriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(TaskPriorityEnum priority) {
        this.priority = priority;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
