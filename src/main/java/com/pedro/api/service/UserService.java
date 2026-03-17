package com.pedro.api.service;

import com.pedro.api.dto.UserDTO;
import com.pedro.api.model.User;
import com.pedro.api.repository.UserRepository;
import com.pedro.api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserDTO insert(UserDTO dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        user = repository.save(user);

        return new UserDTO(user);
    }

    public Page<UserDTO> findAll(Pageable pageable) {

        Page<User> page = repository.findAll(pageable);

        return page.map(UserDTO::new);
    }

    public UserDTO findById(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserDTO(user);
    }

    public UserDTO update(Long id, UserDTO dto) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }

        User user = repository.getReferenceById(id);

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        user = repository.save(user);

        return new UserDTO(user);
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }

        repository.deleteById(id);
    }
}