package com.elotech.task.domain.project.exception;

public class ExcludeProjectWithTasksException extends IllegalArgumentException {
    public ExcludeProjectWithTasksException(String message) {
        super(message);
    }
}
