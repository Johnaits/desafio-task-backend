package com.elotech.task.domain.task.enums;

public enum TaskPriorityEnum {

    LOW(1, "Low"),
    MEDIUM(2, "Medium"),
    HIGH(3, "High"),
    CRITICAL(4, "Critical");

    private final int id;

    private final String description;

    TaskPriorityEnum(int id, String description){
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
