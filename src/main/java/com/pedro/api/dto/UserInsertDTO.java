package com.pedro.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto para criação de um novo usuário")
public class UserInsertDTO extends UserDTO {

    @Schema(description = "Senha do usuário em texto puro", example = "senha123")
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}