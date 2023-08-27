package com.ada.avanadestore.entitity;

import com.ada.avanadestore.dto.CustomerDTO;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    @Column(length = 14, unique = true)
    private String cpf;

    public Customer() {
    }

    public Customer(CustomerDTO dto) {
        super(dto);
        this.cpf = dto.getCpf();
    }

    @Override
    public CustomerDTO toDTO() {
        return new CustomerDTO(id, name, email, "********", birthdate, this.cpf, address.toDTO(), createdAt, updatedAt);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
