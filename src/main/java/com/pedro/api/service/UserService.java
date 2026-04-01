package com.pedro.api.service;

import com.pedro.api.dto.PerfilDTO;
import com.pedro.api.dto.UserDTO;
import com.pedro.api.exception.ResourceNotFoundException;
import com.pedro.api.model.Perfil;
import com.pedro.api.model.User;
import com.pedro.api.repository.PerfilRepository;
import com.pedro.api.repository.UserRepository;
import com.pedro.api.util.Notificador; // 1. Importe a interface
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PerfilRepository perfilRepository;
    private final Notificador notificador; // 2. Declare o Notificador

    // 3. Injete no construtor
    public UserService(UserRepository repository, PerfilRepository perfilRepository, Notificador notificador) {
        this.repository = repository;
        this.perfilRepository = perfilRepository;
        this.notificador = notificador;
    }

    @Transactional
    public UserDTO insert(UserDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);

        if (user.getPerfis().isEmpty()) {
            Perfil defaultPerfil = perfilRepository.getReferenceById(2L);
            user.getPerfis().add(defaultPerfil);
        }

        user = repository.save(user);

        // 4. DISPARAR A NOTIFICAÇÃO
        // O Spring vai usar a implementação que você configurou no ServiceConfig
        notificador.notificar(user, "Sua conta foi criada com sucesso e está pronta para uso!");

        return new UserDTO(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> page = repository.findAll(pageable);
        return page.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }

        User user = repository.getReferenceById(id);
        copyDtoToEntity(dto, user);

        user = repository.save(user);

        // Opcional: Notificar em caso de atualização também
        notificador.notificar(user, "Seus dados foram atualizados no sistema.");

        return new UserDTO(user);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setPhone(dto.getPhone());

        entity.getPerfis().clear();
        for (PerfilDTO perfilDto : dto.getPerfis()) {
            Perfil perfil = perfilRepository.getReferenceById(perfilDto.getId());
            entity.getPerfis().add(perfil);
        }
    }
}