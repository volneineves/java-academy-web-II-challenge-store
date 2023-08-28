package com.ada.avanadestore.entitity;

import com.ada.avanadestore.dto.EmployeeDTO;
import com.ada.avanadestore.enums.EmployeeRoles;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "employees")
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private EmployeeRoles role;

    @OneToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Employee() {
    }


    public Employee(EmployeeDTO dto, Department department) {
        super(dto);
        this.salary = dto.getSalary();
        this.department = department;
        this.role = dto.getRole();
    }


    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public EmployeeRoles getRole() {
        return role;
    }

    public void setRole(EmployeeRoles role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public EmployeeDTO toDTO() {
        return new EmployeeDTO(id, name, email, "********", birthdate, department.getName(), salary, role, address.toDTO(), createdAt, updatedAt);
    }
}
