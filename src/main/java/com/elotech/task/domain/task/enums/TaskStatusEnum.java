package com.elotech.task.domain.task.enums;

public enum TaskStatusEnum{
    TODO(1, "To Do"),
    IN_PROGRESS(2, "In Progress"),
    DONE(3, "Done");

    private final int id;

    private final String description;

    TaskStatusEnum(int id, String description){
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
