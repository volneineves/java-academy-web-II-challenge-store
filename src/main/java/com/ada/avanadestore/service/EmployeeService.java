package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.UserEmailFormDTO;
import com.ada.avanadestore.dto.EmployeeDTO;
import com.ada.avanadestore.entitity.Address;
import com.ada.avanadestore.entitity.Department;
import com.ada.avanadestore.entitity.Employee;
import com.ada.avanadestore.entitity.User;
import com.ada.avanadestore.enums.EmployeeRoles;
import com.ada.avanadestore.event.EmailPublisher;
import com.ada.avanadestore.exception.BadRequestException;
import com.ada.avanadestore.exception.InternalServerException;
import com.ada.avanadestore.exception.ResourceNotFoundException;
import com.ada.avanadestore.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.*;

@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailPublisher emailPublisher;
    private final DepartmentService departmentService;

    public EmployeeService(EmployeeRepository repository, PasswordEncoder passwordEncoder, EmailPublisher emailPublisher, DepartmentService departmentService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.emailPublisher = emailPublisher;
        this.departmentService = departmentService;
    }

    public Employee getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));
    }

    public EmployeeDTO findById(UUID id) {
        return getById(id).toDTO();
    }

    public List<Employee> getAllByRoleAndDepartmentName(EmployeeRoles role, String departmentName) {
        return repository.findAllByRoleAndDepartmentName(role, departmentName);
    }

    public EmployeeDTO create(EmployeeDTO dto) {
        Department department = departmentService.getById(dto.getDepartmentId());
        Employee employee = new Employee(dto, department);
        encodeUserPassword(employee);
        trySaveOrThrowError(employee);
        sendEmailForCreatedUser(employee);
        return employee.toDTO();
    }

    public EmployeeDTO update(UUID id, EmployeeDTO dto) {
        Employee existingEmployee = getById(id);

        existingEmployee.setName(dto.getName() != null ? dto.getName() : existingEmployee.getName());
        existingEmployee.setEmail(dto.getEmail() != null ? dto.getEmail() : existingEmployee.getEmail());
        existingEmployee.setBirthdate(dto.getBirthdate() != null ? dto.getBirthdate() : existingEmployee.getBirthdate());
        existingEmployee.setAddress(dto.getAddress() != null ? new Address(dto.getAddress()) : existingEmployee.getAddress());
        existingEmployee.setSalary(dto.getSalary() != null ? dto.getSalary() : existingEmployee.getSalary());
        existingEmployee.setRole(dto.getRole() != null ? dto.getRole() : existingEmployee.getRole());

        if (dto.getDepartmentId() != existingEmployee.getDepartment().getId()) {
            Department department = departmentService.getById(dto.getDepartmentId());
            existingEmployee.setDepartment(department);
        }

        if (dto.getPassword() != null && dto.getPassword().length() >= 8) {
            existingEmployee.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        trySaveOrThrowError(existingEmployee);
        sendEmailForUpdatedUser(existingEmployee);
        return existingEmployee.toDTO();
    }

    public void deactivate(UUID id) {
        Employee existingEmployee = getById(id);
        existingEmployee.setActive(false);
        trySaveOrThrowError(existingEmployee);
        sendEmailForDisabledUser(existingEmployee);
        existingEmployee.toDTO();
    }


    private void trySaveOrThrowError(Employee employee) {
        try {
            repository.save(employee);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(DATA_INTEGRITY_ERROR);
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

    private void sendEmailForUpdatedUser(User user) {
        UserEmailFormDTO userEmailFormDTO = new UserEmailFormDTO(user.getEmail(), SUBJECT_USER_DATA_UPDATED, MESSAGE_USER_DATA_UPDATED);
        emailPublisher.handleSendEmailEventUser(userEmailFormDTO);
    }
    private void sendEmailForDisabledUser(User user) {
        UserEmailFormDTO userEmailFormDTO = new UserEmailFormDTO(user.getEmail(), SUBJECT_USER_DATA_UPDATED, MESSAGE_USER_DISABLED);
        emailPublisher.handleSendEmailEventUser(userEmailFormDTO);
    }

}
