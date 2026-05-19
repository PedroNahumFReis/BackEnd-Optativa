package com.pedro.api.controller;

import com.pedro.api.dto.CategoryDTO;
import com.pedro.api.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias das tarefas")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping(produces = "application/json")
    @Operation(
            summary = "Criar uma nova categoria",
            description = "Cadastra uma categoria no banco. Apenas administradores têm permissão.",
            responses = {
                    @ApiResponse(description = "Categoria criada com sucesso", responseCode = "201"),
                    @ApiResponse(description = "Erro de validação ou falta de autorização", responseCode = "403")
            }
    )
    public ResponseEntity<CategoryDTO> insert(@RequestBody @Valid CategoryDTO dto) {
        CategoryDTO newDto = service.insert(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDto);
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Listar todas as categorias de forma paginada")
    public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable) {
        Page<CategoryDTO> list = service.findAll(pageable);
        return ResponseEntity.ok(list);
    }
}