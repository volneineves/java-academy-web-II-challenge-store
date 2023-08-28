package com.ada.avanadestore.controller;

import com.ada.avanadestore.dto.CustomerDTO;
import com.ada.avanadestore.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> create(@PathVariable("id") UUID id) {
        CustomerDTO customerDTO = service.findById(id);
        return ResponseEntity.ok(customerDTO);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> create(@RequestBody @Valid CustomerDTO dto) {
        CustomerDTO createdCustomer = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable("id") UUID id, @RequestBody CustomerDTO dto) {
        CustomerDTO updatedCustomer = service.update(id, dto);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable("id") UUID id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
