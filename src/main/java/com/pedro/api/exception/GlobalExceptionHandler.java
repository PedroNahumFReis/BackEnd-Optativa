package com.pedro.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError; // IMPORT NOVO
import org.springframework.web.bind.MethodArgumentNotValidException; // IMPORT NOVO
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Tratamento para Recursos Não Encontrados (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(
            ResourceNotFoundException e,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Resource not found",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // 2. Tratamento para erros de Banco/Email Duplicado (409)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> database(DataIntegrityViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        String message = "Erro de integridade de dados.";
        String rootCause = e.getMostSpecificCause().getMessage();

        if (rootCause.contains("email") || rootCause.contains("UK_") || rootCause.contains("tb_user")) {
            message = "O e-mail informado já está cadastrado em nosso sistema.";
        }

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Database Integrity Error",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // 2.1. Falha ao enviar e-mail (serviço externo) (502)
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<StandardError> email(EmailException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Email Service Error",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // 3. NOVO: Tratamento de Erros de Validação (Ex: @NotBlank, @Email) (422)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; // Código 422

        ValidationError err = new ValidationError(
                Instant.now(),
                status.value(),
                "Validation Error",
                "Erro de validação nos dados enviados.",
                request.getRequestURI()
        );

        // Percorre todos os erros identificados pelo Spring (ex: @Email no dto) e adiciona na nossa lista
        for (FieldError field : e.getBindingResult().getFieldErrors()) {
            err.addFieldMessage(field.getField(), field.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(err);
    }
}