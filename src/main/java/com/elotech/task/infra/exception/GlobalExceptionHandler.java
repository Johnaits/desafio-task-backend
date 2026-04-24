package com.elotech.task.infra.exception;

import com.elotech.task.infra.exception.dto.DefaultErrorDTO;
import com.elotech.task.infra.exception.dto.FieldErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DefaultErrorDTO> handleBadCredentials(BadCredentialsException ex){
        var error = new DefaultErrorDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Não autorizado",
                "E-mail ou senha incorretos"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldErrorDTO>> handleValidationErros(MethodArgumentNotValidException ex){
        List<FieldErrorDTO> errors = ex.getFieldErrors().stream()
                .map(FieldErrorDTO::new)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

    }
}
