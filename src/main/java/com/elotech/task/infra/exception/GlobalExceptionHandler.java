package com.elotech.task.infra.exception;

import com.elotech.task.infra.exception.dto.FieldErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos");
        problemDetail.setTitle("Não autorizado");
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex){

        List<FieldErrorDTO> errors = ex.getFieldErrors().stream()
                .map(FieldErrorDTO::new)
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Argumentos inválidos. Verifique o(s) campo(s) passados");
        problemDetail.setTitle("Erro de validação de dados");
        problemDetail.setProperty("invalidFields", errors);
        return problemDetail;

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex){
        String message = (ex.getMessage() != null) ? ex.getMessage() : "Entidade inexistente";
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Não encontrado");
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex){
        String message = (ex.getMessage() != null) ? ex.getMessage() : "Erro de validação nos dados fornecidos.";
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        problemDetail.setTitle("Argumento inválido");
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleViolationData(DataIntegrityViolationException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Violação na integridade dos dados");
        problemDetail.setTitle("Dado inválido");
        return problemDetail;
    }
}
