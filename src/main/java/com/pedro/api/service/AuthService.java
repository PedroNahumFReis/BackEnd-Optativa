package com.pedro.api.service;

import com.pedro.api.dto.EmailDTO;
import com.pedro.api.dto.RequestTokenDTO;
import com.pedro.api.exception.ResourceNotFoundException;
import com.pedro.api.model.PasswordRecover;
import com.pedro.api.model.User;
import com.pedro.api.repository.PasswordRecoverRepository;
import com.pedro.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.pedro.api.dto.NewPasswordDTO;
import java.util.List;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private EmailService emailService;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Transactional
    public void saveNewPassword(NewPasswordDTO dto) {
        // 1. Busca no banco se existe um token igual a esse e que NÃO esteja expirado
        List<PasswordRecover> list = passwordRecoverRepository.searchValidTokens(dto.getToken(), Instant.now());

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Token not found or expired");
        }

        // 2. Se achou o token, pega o email associado a ele e busca o usuário
        User user = userRepository.findByEmail(list.get(0).getEmail());

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        // 3. Atualiza a senha (criptografando a nova!)
        user.setPassword(encoder.encode(dto.getPassword()));

        // 4. Salva no banco de dados
        userRepository.save(user);
    }


    @Transactional
    public void createRecoverToken(RequestTokenDTO dto) {
        // Busca o usuário pelo e-mail
        User user = userRepository.findByEmail(dto.getEmail());

        // Verifica se ele existe
        if (user == null) {
            throw new ResourceNotFoundException("Email not found");
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

        // Envio real do e-mail de recuperação
        emailService.sendMail(new EmailDTO(dto.getEmail(), "Recuperação de senha", text));
    }
}