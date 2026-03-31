package com.pedro.api.dto;

import com.pedro.api.model.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Instant createdAt;
    private Instant updatedAt;

    // Agora usamos uma lista de DTOs de Perfil
    private List<PerfilDTO> perfis = new ArrayList<>();
    private List<TaskDTO> tasks = new ArrayList<>();

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();

        // Mapeamento de Perfis (Entidade -> DTO)
        if (user.getPerfis() != null) {
            user.getPerfis().forEach(perfil -> this.perfis.add(new PerfilDTO(perfil)));
        }

        // Mapeamento de Tasks (Entidade -> DTO)
        if (user.getTasks() != null) {
            this.tasks = user.getTasks().stream()
                    .map(TaskDTO::new)
                    .collect(Collectors.toList());
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<PerfilDTO> getPerfis() { return perfis; } // Getter atualizado
    public List<TaskDTO> getTasks() { return tasks; }
}