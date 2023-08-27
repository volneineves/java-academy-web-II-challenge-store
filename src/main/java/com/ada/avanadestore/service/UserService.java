package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.EmailFormDTO;
import com.ada.avanadestore.dto.UserDTO;
import com.ada.avanadestore.entitity.User;
import com.ada.avanadestore.event.EmailPublisher;
import com.ada.avanadestore.exception.ResourceNotFoundException;
import com.ada.avanadestore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.*;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailPublisher emailPublisher;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, EmailPublisher emailPublisher) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.emailPublisher = emailPublisher;
    }

    public User getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }

    public UserDTO create(UserDTO dto) {
        User newUser = new User(dto);
        encodeUserPassword(newUser);
        sendEmailForCreatedUser(newUser);
        return repository.save(newUser).toDTO();
    }

    private void encodeUserPassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    private void sendEmailForCreatedUser(User user) {
        EmailFormDTO emailFormDTO = new EmailFormDTO(user.getEmail(), SUBJECT_WELCOME_NEW_USER, MESSAGE_WELCOME_NEW_USER);
        emailPublisher.handleSendEmailEvent(emailFormDTO);
    }
}
