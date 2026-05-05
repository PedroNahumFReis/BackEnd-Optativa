package com.pedro.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto para atualização de um usuário existente")
public class UserUpdateDTO extends UserDTO {

    @Schema(description = "Nova senha do usuário (deixe vazio se não quiser alterar)", example = "novaSenha123")
    private String password;

    public UserUpdateDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}