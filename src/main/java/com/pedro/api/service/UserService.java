package com.pedro.api.service;

import com.pedro.api.dto.UserDTO;
import com.pedro.api.model.User;
import com.pedro.api.model.enums.UserRole;
import com.pedro.api.repository.UserRepository;
import com.pedro.api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserDTO insert(UserDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);

        // Atribui USER como padrão se nenhum cargo for enviado
        if (user.getRoles().isEmpty()) {
            user.getRoles().add(UserRole.USER);
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

        entity.getRoles().clear();
        dto.getRoles().forEach(role -> entity.getRoles().add(role));
    }
}