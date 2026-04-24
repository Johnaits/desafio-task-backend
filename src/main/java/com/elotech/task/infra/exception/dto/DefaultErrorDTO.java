package com.elotech.task.infra.exception.dto;

import java.time.LocalDateTime;

public record DefaultErrorDTO(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message
) {
    public DefaultErrorDTO(Integer status, String error, String message){
        this(LocalDateTime.now(), status, error, message);
    }
}
