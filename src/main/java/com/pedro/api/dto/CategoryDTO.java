package com.pedro.api.dto;

import com.pedro.api.model.Category;

public class CategoryDTO {
    private Long id;
    private String name;

    public CategoryDTO() {}

    public CategoryDTO(Category entity) {
        this.id = entity.getId(); // Agora o símbolo 'getId' será encontrado
        this.name = entity.getName();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}