package com.pedro.api.dto;

import com.pedro.api.model.Perfil;

public class PerfilDTO {
    private Long id;
    private String nome;

    public PerfilDTO() {}

    public PerfilDTO(Perfil entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}