package com.pedro.api.service;

import com.pedro.api.dto.TaskDTO;
import com.pedro.api.exception.ResourceNotFoundException;
import com.pedro.api.model.Category;
import com.pedro.api.model.Task;
import com.pedro.api.model.User;
import com.pedro.api.model.Tag; // Certifique-se de importar a sua entidade Tag
import com.pedro.api.repository.CategoryRepository;
import com.pedro.api.repository.TaskRepository;
import com.pedro.api.repository.UserRepository;
import com.pedro.api.repository.TagRepository; // Importar o TagRepository
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository; // 1. Injetar o TagRepository

    // 2. Atualizar o construtor com o novo repositório
    public TaskService(TaskRepository repository, UserRepository userRepository,
                       CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public TaskDTO insert(TaskDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        Task task = new Task();
        copyDtoToEntity(dto, task);
        task.setUser(user);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
            task.setCategory(category);
        }

        // 3. LÓGICA PARA ASSOCIAR AS TAGS NO INSERT
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            task.getTags().addAll(tags);
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

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setUser(user);
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            task.setCategory(category);
        }

        // 4. LÓGICA PARA ATUALIZAR AS TAGS NO UPDATE
        if (dto.getTagIds() != null) {
            task.getTags().clear(); // Limpa as associações antigas para evitar lixo
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            task.getTags().addAll(tags);
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

    @Transactional
    public TaskDTO updateTags(Long id, Set<Long> tagIds) {
        // 1. Verifica se a tarefa existe
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        // 2. Limpa as tags antigas associadas a essa tarefa
        task.getTags().clear();

        // 3. Se o Front-end enviou IDs, busca as novas tags e adiciona na tarefa
        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> newTags = tagRepository.findAllById(tagIds);
            task.getTags().addAll(newTags);
        }

        // 4. Salva e retorna o DTO atualizado
        task = repository.save(task);
        return new TaskDTO(task);
    }

    private void copyDtoToEntity(TaskDTO dto, Task entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
    }
}