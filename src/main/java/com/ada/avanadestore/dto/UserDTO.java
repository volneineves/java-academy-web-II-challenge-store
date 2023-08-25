package com.ada.avanadestore.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record UserDTO(UUID id, String name, String email, String password, String cpf, LocalDate birthdate, AddressDTO address, Date createdAt, Date updatedAt) {
}
