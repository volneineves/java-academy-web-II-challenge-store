package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.UserDTO;
import com.ada.avanadestore.entitity.User;
import com.ada.avanadestore.exception.ResourceNotFound;
import com.ada.avanadestore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ada.avanadestore.constants.ErrorMessages.USER_NOT_FOUND;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND));
    }

    public UserDTO create(UserDTO dto) {
        User newUser = new User(dto);
        encodeUserPassword(newUser);
        User createdUser = repository.save(newUser);
        return createdUser.toDTO();
    }

    private void encodeUserPassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
}
