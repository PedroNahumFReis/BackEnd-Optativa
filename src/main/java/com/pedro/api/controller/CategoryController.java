package com.pedro.api.controller;

import com.pedro.api.dto.CategoryDTO;
import com.pedro.api.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public CategoryDTO insert(@RequestBody CategoryDTO dto) {
        return service.insert(dto);
    }

    @GetMapping
    public Page<CategoryDTO> findAll(Pageable pageable) {
        return service.findAll(pageable);
    }
}