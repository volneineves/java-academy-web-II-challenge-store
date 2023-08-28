package com.ada.avanadestore.service;

import com.ada.avanadestore.entitity.Department;
import com.ada.avanadestore.exception.ResourceNotFoundException;
import com.ada.avanadestore.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.DEPARTMENT_NOT_FOUND;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public Department getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(DEPARTMENT_NOT_FOUND));
    }
}
