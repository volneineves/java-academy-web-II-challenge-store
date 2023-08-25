package com.ada.avanadestore.controller;

import com.ada.avanadestore.dto.UserDTO;
import com.ada.avanadestore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        UserDTO createdUser = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
