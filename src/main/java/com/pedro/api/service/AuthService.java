package com.pedro.api.service;

import com.pedro.api.dto.RequestTokenDTO;
import com.pedro.api.model.PasswordRecover;
import com.pedro.api.model.User; // Ajuste para o nome da sua entidade (User ou Usuario)
import com.pedro.api.repository.PasswordRecoverRepository;
import com.pedro.api.repository.UserRepository; // Ajuste para o seu repositório
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    // @Autowired
    // private EmailService emailService; // Descomente quando tiver seu EmailService

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Transactional
    public void createRecoverToken(RequestTokenDTO dto) {
        // Busca o usuário pelo e-mail clássico
        User user = userRepository.findByEmail(dto.getEmail());

        // Verifica se ele existe
        if (user == null) {
            throw new RuntimeException("Email not found"); // Ajuste para a sua Exception personalizada, se tiver
        }

        // Gera o token
        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        entity.setEmail(dto.getEmail());

        passwordRecoverRepository.save(entity);

        String text = "Acesse o link para definir uma nova senha (válido por " + tokenMinutes + " minutos):\n\n"
                + recoverUri + token;

        // emailService.sendMail(dto.getEmail(), "Recuperação de senha", text);
        System.out.println("E-MAIL SIMULADO: " + text); // Mock para você testar no console por enquanto
    }
}