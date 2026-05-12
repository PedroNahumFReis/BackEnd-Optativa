package com.pedro.api.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_user")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_user_perfil",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private Set<Perfil> perfis = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    public User() {}

    public User(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    // --- GETTERS E SETTERS PADRÕES ---

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Este método atende tanto ao seu uso padrão quanto à interface UserDetails
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Set<Perfil> getPerfis() { return perfis; }
    public void setPerfis(Set<Perfil> perfis) { this.perfis = perfis; }
    public List<Task> getTasks() { return tasks; }

    // --- MÉTODOS DE LÓGICA ---

    public void addPerfil(Perfil perfil) {
        this.perfis.add(perfil);
    }

    public boolean hasPerfil(String perfilNome) {
        if (this.perfis == null || perfilNome == null) return false;
        return this.perfis.stream()
                .anyMatch(p -> p.getNome().equalsIgnoreCase(perfilNome));
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setUser(this);
    }

    // --- MÉTODOS EXIGIDOS PELO SPRING SECURITY (USERDETAILS) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis; // Retorna a lista de perfis do usuário
    }

    @Override
    public String getUsername() {
        return email; // Mapeia o "username" do Spring para o e-mail no seu banco
    }

    // Os métodos abaixo são verificações de conta do Spring Security.
    // Por padrão, deixamos true. No futuro, se implementar bloqueio de conta, você altera aqui.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}