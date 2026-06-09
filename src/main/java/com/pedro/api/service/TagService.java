package com.pedro.api.service;

import com.pedro.api.dto.TagDTO;
import com.pedro.api.exception.ResourceNotFoundException;
import com.pedro.api.model.Tag;
import com.pedro.api.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository repository;

    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TagDTO insert(TagDTO dto) {
        // Regra de excelência: impede a criação de etiquetas com nomes duplicados
        repository.findByName(dto.getName()).ifPresent(t -> {
            throw new IllegalArgumentException("Tag already exists with name: " + dto.getName());
        });

        Tag entity = new Tag();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new TagDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<TagDTO> findAll() {
        List<Tag> list = repository.findAll();
        return list.stream().map(TagDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TagDTO findById(Long id) {
        Tag entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        return new TagDTO(entity);
    }

    @Transactional
    public TagDTO update(Long id, TagDTO dto) {
        Tag entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new TagDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found with id: " + id);
        }
        repository.deleteById(id);
    }
}