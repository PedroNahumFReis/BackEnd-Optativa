package com.pedro.api.service;

import com.pedro.api.dto.PerfilDTO;
import com.pedro.api.dto.UserDTO;
import com.pedro.api.exception.ResourceNotFoundException;
import com.pedro.api.model.Perfil;
import com.pedro.api.model.User;
import com.pedro.api.repository.PerfilRepository;
import com.pedro.api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PerfilRepository perfilRepository; // 1. Nova dependência

    public UserService(UserRepository repository, PerfilRepository perfilRepository) {
        this.repository = repository;
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public UserDTO insert(UserDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);

        // Lógica de perfil padrão: Se não vier perfis, buscamos o ID 2 (ex: ROLE_USER)
        // Certifique-se de que o ID 2 existe no seu import.sql como ROLE_USER
        if (user.getPerfis().isEmpty()) {
            Perfil defaultPerfil = perfilRepository.getReferenceById(2L);
            user.getPerfis().add(defaultPerfil);
        }

        user = repository.save(user);
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

        // 2. Limpamos os perfis antigos e adicionamos os novos baseados no ID do DTO
        entity.getPerfis().clear();
        for (PerfilDTO perfilDto : dto.getPerfis()) {
            // getReferenceById é mais performático que findById aqui pois não faz um SELECT imediato
            Perfil perfil = perfilRepository.getReferenceById(perfilDto.getId());
            entity.getPerfis().add(perfil);
        }
    }
}