package com.ada.avanadestore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.*;

public class UserDTO {

    private final UUID id;

    @NotBlank(message = NAME_CANNOT_BE_EMPTY_NULL)
    private final String name;

    @Email(message = INVALID_EMAIL_FORMAT)
    @NotNull(message = EMAIL_CANNOT_BE_NULL)
    private final String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = PASSWORD_CANNOT_BE_NULL)
    @Size(min = 8, message = PASSWORD_MIN_LENGTH_8)
    private final String password;

    @NotNull(message = BIRTHDATE_CANNOT_BE_NULL)
    private final LocalDate birthdate;

    @NotNull(message = ADDRESS_CANNOT_BE_NULL)
    @Valid
    private final AddressDTO address;

    private final Date createdAt;
    private final Date updatedAt;

    public UserDTO(UUID id,
                   String name,
                   String email,
                   String password,
                   LocalDate birthdate,
                   AddressDTO address,
                   Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
