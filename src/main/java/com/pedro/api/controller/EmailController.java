package com.pedro.api.controller;

import com.pedro.api.dto.EmailDTO;
import com.pedro.api.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/email")
@Tag(name = "E-mails", description = "Endpoints para envio e gerenciamento de notificações por e-mail")
public class EmailController {

    private final EmailService emailService;

    // Padronizado usando injeção via construtor, assim como seus outros controllers
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // Controle de acesso fino: permite que administradores e usuários comuns enviem e-mails
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping(produces = "application/json")
    @Operation(
            summary = "Enviar um e-mail manual",
            description = "Dispara um e-mail com assunto e corpo personalizados baseado no DTO enviado.",
            responses = {
                    @ApiResponse(description = "E-mail enviado com sucesso (Sem conteúdo de retorno)", responseCode = "204"),
                    @ApiResponse(description = "Erro de validação nos dados ou falha de autenticação", responseCode = "403")
            }
    )
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailDTO emailDTO) {
        emailService.sendMail(emailDTO);
        return ResponseEntity.noContent().build();
    }
}