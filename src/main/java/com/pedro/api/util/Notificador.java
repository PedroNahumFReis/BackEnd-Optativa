package com.pedro.api.util;

import com.pedro.api.model.User;

public interface Notificador {
    void notificar(User usuario, String mensagem);
}