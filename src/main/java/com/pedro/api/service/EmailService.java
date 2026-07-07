package com.pedro.api.service;

import com.pedro.api.dto.EmailDTO;
import com.pedro.api.exception.EmailException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(@Valid EmailDTO emailDTO) {
        try {
            logger.info("Enviando e-mail para: {}", emailDTO.getTo());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(emailDTO.getTo());
            message.setSubject(emailDTO.getSubject());
            message.setText(emailDTO.getBody());

            mailSender.send(message);
            logger.info("E-mail enviado com sucesso!");

        } catch (Exception e) {
            // Antes o erro era engolido (só printStackTrace), então uma falha de envio
            // passava despercebida. Agora propagamos para o handler global tratar.
            logger.error("Falha ao enviar e-mail para {}: {}", emailDTO.getTo(), e.getMessage());
            throw new EmailException("Falha ao enviar e-mail: " + e.getMessage(), e);
        }
    }
}