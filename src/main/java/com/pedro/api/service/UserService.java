package com.pedro.api.service;

import com.pedro.api.controller.UserController;
import com.pedro.api.dto.PerfilDTO;
import com.pedro.api.dto.UserDTO;
import com.pedro.api.dto.UserInsertDTO;
import com.pedro.api.dto.UserUpdateDTO;
import com.pedro.api.exception.ResourceNotFoundException;
import com.pedro.api.model.Perfil;
import com.pedro.api.model.User;
import com.pedro.api.projections.UserDetailsProjection;
import com.pedro.api.repository.PerfilRepository;
import com.pedro.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService implements UserDetailsService { // Adicionado o implements UserDetailsService

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final PerfilRepository perfilRepository;
    private final AtivacaoUsuarioService ativacaoUsuarioService;

    @Autowired
    private PasswordEncoder encoder;

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
    public UserDTO insert(UserInsertDTO dto) {
        logger.info("Iniciando inserção de novo usuário: {}", dto.getEmail());

        User user = new User();
        copyDtoToEntity(dto, user);

        user.setPassword(encoder.encode(dto.getPassword()));

        if (user.getPerfis().isEmpty()) {
            logger.warn("Nenhum perfil enviado. Atribuindo perfil padrão (ID 2).");
            Perfil defaultPerfil = perfilRepository.getReferenceById(2L);
            user.addPerfil(defaultPerfil);
        }

        user = repository.save(user);

        logger.info("Usuário salvo com sucesso. ID gerado: {}", user.getId());
        ativacaoUsuarioService.ativar(user, "Conta criada com sucesso!");

        return new UserDTO(user)
                .add(linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel())
                .add(linkTo(methodOn(UserController.class).list(defaultPageable)).withRel("Ver todos"));
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        logger.info("Iniciando atualização do usuário ID: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        copyDtoToEntity(dto, user);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(encoder.encode(dto.getPassword()));
        }

        user = repository.save(user);

        logger.info("Usuário ID {} atualizado com sucesso.", id);
        ativacaoUsuarioService.ativar(user, "Seus dados foram atualizados no sistema.");

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
        entity.setPhone(dto.getPhone());

        entity.getPerfis().clear();
        if (dto.getPerfis() != null) {
            for (PerfilDTO perfilDto : dto.getPerfis()) {
                Perfil perfil = perfilRepository.getReferenceById(perfilDto.getId());
                entity.addPerfil(perfil);
            }
        }
    }

    // --- INTEGRAÇÃO COM O SPRING SECURITY ---

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Spring Security: Buscando usuário e permissões para o email: {}", username);

        List<UserDetailsProjection> dados = repository.searchUserAndRolesByEmail(username);

        if (dados.isEmpty()) {
            logger.error("Falha na autenticação: Usuário não encontrado ({})", username);
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        User user = new User();
        user.setEmail(dados.get(0).getUsername());
        user.setPassword(dados.get(0).getPassword());

        // Mapeia as Roles (Perfis) retornadas na consulta para o objeto User
        for (UserDetailsProjection projection : dados) {
            user.addPerfil(new Perfil(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }
}