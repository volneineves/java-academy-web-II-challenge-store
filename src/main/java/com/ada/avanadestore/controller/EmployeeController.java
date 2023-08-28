package com.ada.avanadestore.controller;

import com.ada.avanadestore.dto.EmployeeDTO;
import com.ada.avanadestore.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable("id") UUID id) {
        EmployeeDTO employeeDTO = service.findById(id);
        return ResponseEntity.ok(employeeDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@RequestBody @Valid EmployeeDTO dto) {
        EmployeeDTO createdEmployee = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable("id") UUID id, @RequestBody EmployeeDTO dto) {
        EmployeeDTO updatedEmployee = service.update(id, dto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable("id") UUID id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
