package com.pedro.api.service;

import com.pedro.api.dto.UserDTO;
import com.pedro.api.model.User;
import com.pedro.api.repository.UserRepository;
import com.pedro.api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

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

    public List<UserDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(UserDTO::new)
                .toList();
    }

    public UserDTO findById(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserDTO(user);
    }

    public UserDTO update(Long id, UserDTO dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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