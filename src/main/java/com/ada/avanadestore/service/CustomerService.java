package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.CustomerDTO;
import com.ada.avanadestore.dto.UserEmailFormDTO;
import com.ada.avanadestore.entitity.Address;
import com.ada.avanadestore.entitity.Customer;
import com.ada.avanadestore.entitity.User;
import com.ada.avanadestore.event.EmailPublisher;
import com.ada.avanadestore.exception.BadRequestException;
import com.ada.avanadestore.exception.DatabaseException;
import com.ada.avanadestore.exception.InternalServerException;
import com.ada.avanadestore.exception.ResourceNotFoundException;
import com.ada.avanadestore.handler.CustomExceptionHandler;
import com.ada.avanadestore.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.*;
import static com.ada.avanadestore.constants.RegexPatterns.POSTGRES_DB_ERROR_PATTERN;
import static com.ada.avanadestore.util.JavaUtil.doRegexPattern;

@Service
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailPublisher emailPublisher;

    public CustomerService(CustomerRepository repository, PasswordEncoder passwordEncoder, EmailPublisher emailPublisher) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.emailPublisher = emailPublisher;
    }

    public Customer getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND));
    }

    public CustomerDTO findById(UUID id) {
        return getById(id).toDTO();
    }

    public CustomerDTO create(CustomerDTO dto) {
        Customer customer = new Customer(dto);
        encodeUserPassword(customer);
        sendEmailForCreatedUser(customer);
        trySaveOrThrowError(customer);
        return customer.toDTO();
    }

    public CustomerDTO update(UUID id, CustomerDTO dto) {
        Customer existingCustomer = getById(id);

        existingCustomer.setName(dto.getName() != null ? dto.getName() : existingCustomer.getName());
        existingCustomer.setEmail(dto.getEmail() != null ? dto.getEmail() : existingCustomer.getEmail());
        existingCustomer.setCpf(dto.getCpf() != null ? dto.getCpf() : existingCustomer.getCpf());
        existingCustomer.setBirthdate(dto.getBirthdate() != null ? dto.getBirthdate() : existingCustomer.getBirthdate());
        existingCustomer.setAddress(dto.getAddress() != null ? new Address(dto.getAddress()) : existingCustomer.getAddress());

        if (dto.getPassword() != null && dto.getPassword().length() >= 8) {
            existingCustomer.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        trySaveOrThrowError(existingCustomer);
        return existingCustomer.toDTO();
    }

    public void deactivate(UUID id) {
        Customer existingCustomer = getById(id);
        existingCustomer.setActive(false);
        trySaveOrThrowError(existingCustomer);
        existingCustomer.toDTO();
    }


    private void trySaveOrThrowError(Customer customer) {
        try {
            repository.save(customer);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage(), e.getRootCause());
        } catch (Exception e) {
            throw new InternalServerException(INTERNAL_SERVER_ERROR);
        }
    }

    private void encodeUserPassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    private void sendEmailForCreatedUser(User user) {
        UserEmailFormDTO userEmailFormDTO = new UserEmailFormDTO(user.getEmail(), SUBJECT_WELCOME_NEW_USER, MESSAGE_WELCOME_NEW_USER);
        emailPublisher.handleSendEmailEventUser(userEmailFormDTO);
    }
}
