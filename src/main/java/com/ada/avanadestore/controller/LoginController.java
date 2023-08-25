package com.ada.avanadestore.controller;

import com.ada.avanadestore.dto.LoginDTO;
import com.ada.avanadestore.dto.TokenDTO;
import com.ada.avanadestore.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO dto) {
        TokenDTO response = service.login(dto);
        return ResponseEntity.ok(response);
    }
}
