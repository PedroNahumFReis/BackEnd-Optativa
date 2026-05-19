package com.pedro.api.controller;

import com.pedro.api.dto.TaskDTO;
import com.pedro.api.service.TaskService;
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
@RequestMapping("/tasks")
@Tag(name = "Tarefas", description = "Endpoints para gerenciamento do ciclo de vida das tarefas")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Listar todas as tarefas de forma paginada")
    public ResponseEntity<Page<TaskDTO>> list(Pageable pageable) {
        Page<TaskDTO> list = service.findAll(pageable);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            summary = "Buscar tarefa por ID",
            responses = {
                    @ApiResponse(description = "Tarefa localizada com sucesso", responseCode = "200"),
                    @ApiResponse(description = "Tarefa não encontrada", responseCode = "404")
            }
    )
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        TaskDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    // Tanto ADMIN quanto USER podem criar tarefas
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping(produces = "application/json")
    @Operation(
            summary = "Criar uma nova tarefa",
            description = "Cadastra uma nova tarefa vinculada a um usuário e categoria.",
            responses = {
                    @ApiResponse(description = "Tarefa criada com sucesso", responseCode = "201"),
                    @ApiResponse(description = "Dados inválidos ou erro de autorização", responseCode = "403")
            }
    )
    public ResponseEntity<TaskDTO> insert(@RequestBody @Valid TaskDTO dto) {
        TaskDTO newDto = service.insert(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDto);
    }

    // Tanto ADMIN quanto USER podem atualizar tarefas
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Atualizar dados de uma tarefa existente")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody @Valid TaskDTO dto) {
        TaskDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    // Apenas administradores podem deletar de forma definitiva uma tarefa
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma tarefa do sistema permanentemente")
    @ApiResponse(description = "Tarefa removida com sucesso", responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}