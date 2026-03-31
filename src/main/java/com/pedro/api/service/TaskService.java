package com.pedro.api.service;

import com.pedro.api.dto.TaskDTO;
import com.pedro.api.exception.ResourceNotFoundException;
import com.pedro.api.model.Category;
import com.pedro.api.model.Task;
import com.pedro.api.model.User;
import com.pedro.api.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository; // 1. Injetar o novo Repository

    // Atualizar o construtor para incluir o categoryRepository
    public TaskService(TaskRepository repository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public TaskDTO insert(TaskDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        Task task = new Task();
        copyDtoToEntity(dto, task);

        task.setUser(user);

        // 2. Lógica para associar a Categoria no Insert
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
            task.setCategory(category);
        }

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

        // Atualizar Usuário se necessário
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setUser(user);
        }

        // 3. Lógica para atualizar a Categoria se necessário
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            task.setCategory(category);
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

    private void copyDtoToEntity(TaskDTO dto, Task entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
    }
}