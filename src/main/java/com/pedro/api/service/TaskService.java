package com.pedro.api.service;

import com.pedro.api.dto.TaskDTO;
import com.pedro.api.exception.ResourceNotFoundException;
import com.pedro.api.model.Task;
import com.pedro.api.model.User;
import com.pedro.api.repository.TaskRepository;
import com.pedro.api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final UserRepository userRepository;

    // Injeção via construtor (seguindo seu padrão)
    public TaskService(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TaskDTO insert(TaskDTO dto) {
        // Buscamos o usuário para associar à tarefa
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        Task task = new Task();
        copyDtoToEntity(dto, task);
        task.setUser(user); // Define o dono da tarefa

        task = repository.save(task);
        return new TaskDTO(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(Pageable pageable) {
        Page<Task> page = repository.findAll(pageable);
        return page.map(TaskDTO::new);
    }

    @Transactional(readOnly = true)
    public TaskDTO findById(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return new TaskDTO(task);
    }

    @Transactional
    public TaskDTO update(Long id, TaskDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }

        Task task = repository.getReferenceById(id);
        copyDtoToEntity(dto, task);

        // Se o DTO trouxer um novo userId, atualizamos o dono
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setUser(user);
        }

        task = repository.save(task);
        return new TaskDTO(task);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }
        repository.deleteById(id);
    }

    // Método auxiliar para manter o código limpo (DRY - Don't Repeat Yourself)
    private void copyDtoToEntity(TaskDTO dto, Task entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
    }
}