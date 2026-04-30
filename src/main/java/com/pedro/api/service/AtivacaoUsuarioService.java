package com.pedro.api.service;

import com.pedro.api.dto.EmailDTO;
import com.pedro.api.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtivacaoUsuarioService {

    // 1. Troquei o System.out pelo Logger profissional
    private static final Logger logger = LoggerFactory.getLogger(AtivacaoUsuarioService.class);

    // 2. Injetando o EmailService que usa o JavaMailSender (Gmail)
    @Autowired
    private EmailService emailService;

    public AtivacaoUsuarioService() {
        // O log de construção agora é via logger
    }

    public void ativar(User usuario, String mensagem) {
        logger.info("Preparando ativação do usuário: {}", usuario.getName());

        // 3. Montando o "pacote" (DTO) para o envio de e-mail real
        String assunto = "Bem-vindo ao Sistema - " + usuario.getName();
        String corpo = String.format("Olá %s! Sua conta foi criada com sucesso. Mensagem do sistema: %s",
                usuario.getName(), mensagem);

        EmailDTO emailDTO = new EmailDTO(usuario.getEmail(), assunto, corpo);

        // 4. Disparando o e-mail via Gmail
        emailService.sendMail(emailDTO);

        logger.info("Ativação concluída para: {}", usuario.getEmail());
    }

    @PostConstruct
    public void init() {
        logger.info("Bean AtivacaoUsuarioService PRONTO.");
    }

    @PreDestroy
    public void destroy(){
        logger.info("Finalizando AtivacaoUsuarioService...");
    }
}