package com.pedro.api.dto;

import com.pedro.api.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "Objeto de transferência de dados do Usuário")
public class UserDTO {

    @Schema(description = "ID único gerado pelo banco de dados", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "Pedro Nahum")
    private String name;

    @Schema(description = "Endereço de e-mail válido", example = "pedro@email.com")
    private String email;

    @Schema(description = "Senha do usuário (não retornada em buscas)", example = "********")
    private String password;

    @Schema(description = "Telefone para contato e notificações", example = "31999999999")
    private String phone;

    @Schema(description = "Data de criação do registro")
    private Instant createdAt;

    @Schema(description = "Data da última atualização")
    private Instant updatedAt;

    @Schema(description = "Lista de perfis/papéis atribuídos ao usuário")
    private List<PerfilDTO> perfis = new ArrayList<>();

    @Schema(description = "Lista de tarefas vinculadas ao usuário")
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

        if (user.getPerfis() != null) {
            user.getPerfis().forEach(perfil -> this.perfis.add(new PerfilDTO(perfil)));
        }

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
    public List<PerfilDTO> getPerfis() { return perfis; }
    public List<TaskDTO> getTasks() { return tasks; }
}