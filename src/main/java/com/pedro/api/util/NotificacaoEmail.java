package com.pedro.api.util;

import com.pedro.api.model.User;
import org.springframework.stereotype.Component;

public class NotificacaoEmail implements Notificador {

    private boolean caixaAlta = false; // Define se a mensagem será em CAPS LOCK
    private String servidorSmtp;

    // Construtor que o professor criou
    public NotificacaoEmail(String servidorSmtp) {
        this.servidorSmtp = servidorSmtp;
        System.out.println("NotificacaoEmail criado com sucesso no servidor: " + servidorSmtp);
    }

    @Override
    public void notificar(User usuario, String mensagem) {
        if (this.caixaAlta) {
            mensagem = mensagem.toUpperCase();
        }

        // Completando o printf que estava faltando no seu código
        System.out.printf("Notificando %s através do e-mail %s no servidor %s: %s\n",
                usuario.getName(),
                usuario.getEmail(),
                this.servidorSmtp,
                mensagem);
    }

    // Método para permitir mudar o comportamento (caixa alta)
    public void setCaixaAlta(boolean caixaAlta) {
        this.caixaAlta = caixaAlta;
    }
}