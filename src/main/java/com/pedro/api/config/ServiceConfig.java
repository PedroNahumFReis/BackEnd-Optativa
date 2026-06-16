package com.pedro.api.config;

import com.pedro.api.annotation.TipoDoNotificador;
import com.pedro.api.constant.TipoDeNotificacao;
import com.pedro.api.util.NotificacaoEmail;
import com.pedro.api.util.NotificacaoSMS;
import com.pedro.api.util.Notificador;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ServiceConfig {

    @Profile("prod")
    @TipoDoNotificador(TipoDeNotificacao.EMAIL)
    @Bean
    public Notificador notificacaoEmail(
            @Value("${notificador.email.host}") String servidorSMTP) {
        NotificacaoEmail notificacaoEmail = new NotificacaoEmail(servidorSMTP);
        notificacaoEmail.setCaixaAlta(true);
        return notificacaoEmail;
    }

    @Profile("dev")
    @TipoDoNotificador(TipoDeNotificacao.SMS)
    @Bean
    public Notificador notificacaoSMS() {
        return new NotificacaoSMS();
    }
}