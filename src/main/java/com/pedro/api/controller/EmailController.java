package com.pedro.api.controller;

import com.pedro.api.dto.EmailDTO;
import com.pedro.api.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailDTO emailDTO) {
        emailService.sendMail(emailDTO);
        return ResponseEntity.noContent().build();
    }
}