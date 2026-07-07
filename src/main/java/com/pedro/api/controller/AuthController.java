package com.pedro.api.controller;

import com.pedro.api.dto.RequestTokenDTO;
import com.pedro.api.dto.UserDTO;
import com.pedro.api.dto.UserInsertDTO;
import com.pedro.api.service.AuthService;
import com.pedro.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pedro.api.dto.NewPasswordDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/signup", produces = "application/json")
    public ResponseEntity<UserDTO> signup(@Valid @RequestBody UserInsertDTO dto) {
        UserDTO newDto = userService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDto);
    }

    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody RequestTokenDTO dto) {
        authService.createRecoverToken(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/new-password")
    public ResponseEntity<Void> newPassword(@Valid @RequestBody NewPasswordDTO dto) {
        authService.saveNewPassword(dto);
        return ResponseEntity.noContent().build(); // Retorna 204 Sucesso
    }

}