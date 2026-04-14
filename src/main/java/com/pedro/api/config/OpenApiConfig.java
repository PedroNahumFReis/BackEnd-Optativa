package com.pedro.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pedro API Management")
                        .description("API para gerenciamento de usuários e notificações desenvolvida no curso de Spring Boot.")
                        .version("1.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://github.com/PedroNahumFReis")
                        )
                );
    }
}