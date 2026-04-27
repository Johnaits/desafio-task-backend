package com.elotech.task.domain.task;

import com.elotech.task.domain.task.enums.TaskStatusEnum;
import com.elotech.task.domain.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "task_history")
@EntityListeners(AuditingEntityListener.class)
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_task", referencedColumnName = "id", nullable = false)
    private Task task;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_assignee", referencedColumnName = "id")
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_changed", referencedColumnName = "id", nullable = false)
    private User userChanged;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public TaskHistory(){

    }

    public TaskHistory(
            Task task,
            TaskStatusEnum status,
            User assignee,
            User userChanged
    ){
        this.task = task;
        this.status = status;
        this.assignee = assignee;
        this.userChanged = userChanged;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
