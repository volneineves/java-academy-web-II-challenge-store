package com.ada.avanadestore.dto;

import com.ada.avanadestore.enums.EmployeeRoles;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.*;

public class EmployeeDTO extends UserDTO {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = DEPARTMENT_NOT_NULL)
    private UUID departmentId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String department;

    @Positive(message = SALARY_POSITIVE)
    @NotNull(message = SALARY_NOT_NULL)
    private final BigDecimal salary;

    @NotNull(message = ROLE_NOT_NULL)
    private final EmployeeRoles role;

    public EmployeeDTO(UUID id,
                       String name,
                       String email,
                       String password,
                       LocalDate birthdate,
                       String department,
                       BigDecimal salary,
                       EmployeeRoles role,
                       AddressDTO address,
                       Date createdAt, Date updatedAt) {
        super(id, name, email, password, birthdate, address, createdAt, updatedAt);
        this.department = department;
        this.salary = salary;
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public EmployeeRoles getRole() {
        return role;
    }
}
