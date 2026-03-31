package com.pedro.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException; // IMPORT ESSENCIAL
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 2. ADICIONE O SEU NOVO CÓDIGO AQUI (Tratamento para erros de Banco/Email Duplicado - 409)
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
}