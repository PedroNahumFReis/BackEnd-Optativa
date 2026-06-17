package com.pedro.api.controller;

import com.pedro.api.dto.RequestTokenDTO;
import com.pedro.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody RequestTokenDTO dto) {
        authService.createRecoverToken(dto);
        return ResponseEntity.noContent().build();
    }
}