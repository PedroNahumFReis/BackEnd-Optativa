package com.pedro.api.annotation;

import com.pedro.api.constant.TipoDeNotificacao;
import org.springframework.beans.factory.annotation.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Qualifier // Ela funciona como um qualificador
public @interface TipoDoNotificador {
    TipoDeNotificacao value();
}