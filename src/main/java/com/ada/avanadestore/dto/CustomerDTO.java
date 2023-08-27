package com.ada.avanadestore.dto;

import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.INVALID_CPF_FORMAT;

public class CustomerDTO extends UserDTO {

    @CPF(message = INVALID_CPF_FORMAT)
    private final String cpf;

    public CustomerDTO(UUID id, String name, String email, String password, LocalDate birthdate, String cpf, AddressDTO address, Date createdAt, Date updatedAt) {
        super(id, name, email, password, birthdate, address, createdAt, updatedAt);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }
}
