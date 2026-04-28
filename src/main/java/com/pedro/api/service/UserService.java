package com.pedro.api.service;

import com.pedro.api.controller.UserController;
import com.pedro.api.dto.PerfilDTO;
import com.pedro.api.dto.UserDTO;
import com.pedro.api.exception.ResourceNotFoundException;
import com.pedro.api.model.Perfil;
import com.pedro.api.model.User;
import com.pedro.api.repository.PerfilRepository;
import com.pedro.api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Imports estáticos para o HATEOAS ficar limpo
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    private final UserRepository repository;
    private final PerfilRepository perfilRepository;
    private final AtivacaoUsuarioService ativacaoUsuarioService;

    private final Pageable defaultPageable = PageRequest.of(0, 10, Sort.by("name"));

    public UserService(UserRepository repository, PerfilRepository perfilRepository, AtivacaoUsuarioService ativacaoUsuarioService) {
        this.repository = repository;
        this.perfilRepository = perfilRepository;
        this.ativacaoUsuarioService = ativacaoUsuarioService;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> page = repository.findAll(pageable);
        return page.map(user -> new UserDTO(user)
                .add(linkTo(methodOn(UserController.class).findById(user.getId())).withRel("Detalhes"))
        );
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserDTO(user)
                .add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel())
                .add(linkTo(methodOn(UserController.class).list(defaultPageable)).withRel("Lista Completa"))
                .add(linkTo(methodOn(UserController.class).delete(id)).withRel("Excluir Usuário"));
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
        ativacaoUsuarioService.ativar(user, "Conta criada com sucesso!");

        return new UserDTO(user)
                .add(linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel())
                .add(linkTo(methodOn(UserController.class).list(defaultPageable)).withRel("Ver todos"));
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }

        User user = repository.getReferenceById(id);
        copyDtoToEntity(dto, user);
        user = repository.save(user);

        ativacaoUsuarioService.ativar(user, "Dados atualizados.");

        return new UserDTO(user)
                .add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
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