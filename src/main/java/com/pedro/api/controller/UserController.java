package com.pedro.api.controller;

import com.pedro.api.dto.UserDTO;
import com.pedro.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários e notificações")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(produces = "application/json")
    @Operation(
            summary = "Criar um novo usuário",
            description = "Cadastra um usuário no banco e dispara automaticamente uma notificação de boas-vindas.",
            responses = {
                    @ApiResponse(description = "Usuário criado com sucesso", responseCode = "201"),
                    @ApiResponse(description = "Erro de validação nos dados enviados", responseCode = "400")
            }
    )
    public ResponseEntity<UserDTO> insert(@RequestBody UserDTO dto) {
        UserDTO newDto = service.insert(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDto);
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Listar todos os usuários de forma paginada")
    public ResponseEntity<Page<UserDTO>> list(Pageable pageable) {
        Page<UserDTO> list = service.findAll(pageable);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            summary = "Buscar usuário por ID",
            responses = {
                    @ApiResponse(description = "Usuário encontrado", responseCode = "200"),
                    @ApiResponse(description = "Usuário não localizado", responseCode = "404")
            }
    )
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Atualizar dados de um usuário existente")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        UserDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover um usuário do sistema")
    @ApiResponse(description = "Usuário removido", responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}