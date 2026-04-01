package com.pedro.api.config;

import com.pedro.api.util.NotificacaoEmail;
import com.pedro.api.util.Notificador;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public Notificador notificador() {
        // Aqui você define o servidor manualmente, como o professor sugeriu
        NotificacaoEmail notificacaoEmail = new NotificacaoEmail("smtp.pedro.com.br");
        notificacaoEmail.setCaixaAlta(true); // Se quiser testar o comportamento
        return notificacaoEmail;
    }
}