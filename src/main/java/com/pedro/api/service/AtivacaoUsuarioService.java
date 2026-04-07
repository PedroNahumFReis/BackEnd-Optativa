package com.pedro.api.service;

import com.pedro.api.model.User;
import com.pedro.api.util.Notificador;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class AtivacaoUsuarioService {

    private final Notificador notificador;

    public AtivacaoUsuarioService(Notificador notificador) {
        this.notificador = notificador;
        System.out.println("Iniciando AtivacaoUsuarioService...");
    }

    // AQUI ESTAVA O ERRO: Precisamos dos dois parâmetros!
    public void ativar(User usuario, String mensagem) {
        notificador.notificar(usuario, mensagem);
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean AtivacaoUsuarioService PRONTO.");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Finalizando AtivacaoUsuarioService...");
    }
}