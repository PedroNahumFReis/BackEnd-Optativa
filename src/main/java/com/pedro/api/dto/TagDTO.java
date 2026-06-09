package com.pedro.api.dto;

import com.pedro.api.model.Tag;

public class TagDTO {
    private Long id;
    private String name;

    public TagDTO() {}

    public TagDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Construtor para converter a Entidade do banco para DTO automaticamente
    public TagDTO(Tag entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}