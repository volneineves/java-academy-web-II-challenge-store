package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.UserDTO;
import com.ada.avanadestore.entitity.User;
import com.ada.avanadestore.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserDTO create(UserDTO dto) {
        User newUser = new User(dto);
        User createdUser = repository.save(newUser);
        return createdUser.toDTO();
    }
}
