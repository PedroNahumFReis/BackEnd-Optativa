package com.pedro.api.util;

import com.pedro.api.model.User;

public class NotificacaoSMS implements Notificador {

    @Override
    public void notificar(User usuario, String mensagem) {
        System.out.printf("Notificando %s por SMS através do número %s: %s\n",
                usuario.getName(), usuario.getPhone(), mensagem);
    }
}