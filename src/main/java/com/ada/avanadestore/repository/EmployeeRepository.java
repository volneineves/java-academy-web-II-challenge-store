package com.ada.avanadestore.repository;

import com.ada.avanadestore.entitity.Employee;
import com.ada.avanadestore.enums.EmployeeRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findAllByRoleAndDepartmentName(EmployeeRoles role, String departmentName);
}
