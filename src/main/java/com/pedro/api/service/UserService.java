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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    // 1. Instância do Logger para monitoramento
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
        logger.info("Consultando lista de usuários paginada. Página: {}, Tamanho: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<User> page = repository.findAll(pageable);
        return page.map(user -> new UserDTO(user)
                .add(linkTo(methodOn(UserController.class).findById(user.getId())).withRel("Detalhes"))
        );
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        logger.info("Buscando usuário pelo ID: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Erro: Usuário com ID {} não foi encontrado.", id);
                    return new ResourceNotFoundException("User not found");
                });

        return new UserDTO(user)
                .add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel())
                .add(linkTo(methodOn(UserController.class).list(defaultPageable)).withRel("Lista Completa"))
                .add(linkTo(methodOn(UserController.class).delete(id)).withRel("Excluir Usuário"));
    }

    @Transactional
    public UserDTO insert(UserDTO dto) {
        logger.info("Iniciando inserção de novo usuário: {}", dto.getEmail());

        User user = new User();
        copyDtoToEntity(dto, user);

        if (user.getPerfis().isEmpty()) {
            logger.warn("Nenhum perfil enviado. Atribuindo perfil padrão (ID 2).");
            Perfil defaultPerfil = perfilRepository.getReferenceById(2L);
            user.getPerfis().add(defaultPerfil);
        }

        user = repository.save(user);

        logger.info("Usuário salvo com sucesso. ID gerado: {}", user.getId());

        ativacaoUsuarioService.ativar(user, "Conta criada com sucesso!");

        return new UserDTO(user)
                .add(linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel())
                .add(linkTo(methodOn(UserController.class).list(defaultPageable)).withRel("Ver todos"));
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        logger.info("Iniciando atualização do usuário ID: {}", id);

        if (!repository.existsById(id)) {
            logger.error("Falha na atualização: Usuário ID {} não existe.", id);
            throw new ResourceNotFoundException("User not found");
        }

        User user = repository.getReferenceById(id);
        copyDtoToEntity(dto, user);
        user = repository.save(user);

        logger.info("Usuário ID {} atualizado com sucesso.", id);
        ativacaoUsuarioService.ativar(user, "Dados atualizados.");

        return new UserDTO(user)
                .add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
    }

    @Transactional
    public void delete(Long id) {
        logger.warn("Solicitação de exclusão para o usuário ID: {}", id);

        if (!repository.existsById(id)) {
            logger.error("Falha na exclusão: Usuário ID {} não existe.", id);
            throw new ResourceNotFoundException("User not found");
        }

        repository.deleteById(id);
        logger.info("Usuário ID {} removido do banco de dados.", id);
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