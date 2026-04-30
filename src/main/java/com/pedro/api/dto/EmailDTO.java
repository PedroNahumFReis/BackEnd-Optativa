package com.pedro.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para envio de e-mail")
public class EmailDTO {

    @NotBlank
    @Email
    @Schema(description = "Destinatário", example = "cliente@email.com")
    private String to;

    @NotBlank
    @Schema(description = "Assunto", example = "Bem-vindo ao Sistema")
    private String subject;

    @NotBlank
    @Schema(description = "Conteúdo da mensagem", example = "Olá, sua conta foi ativada!")
    private String body;

    // Construtores, Getters e Setters (conforme o código do professor)
    public EmailDTO() {}
    public EmailDTO(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}