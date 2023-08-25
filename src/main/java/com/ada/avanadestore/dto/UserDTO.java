package com.ada.avanadestore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static com.ada.avanadestore.constants.ErrorMessages.*;
import static com.ada.avanadestore.constants.RegexPatterns.CPF_FORMAT;


public record UserDTO(UUID id,

                      @NotBlank(message = NAME_CANNOT_BE_EMPTY_NULL) String name,

                      @Email(message = INVALID_EMAIL_FORMAT) @NotNull(message = EMAIL_CANNOT_BE_NULL) String email,

                      @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotNull(message = PASSWORD_CANNOT_BE_NULL) @Size(min = 8, message = PASSWORD_MIN_LENGTH_8) String password,

                      @NotNull(message = CPF_CANNOT_BE_NULL) @Pattern(regexp = CPF_FORMAT, message = INVALID_CPF_FORMAT) String cpf,

                      @NotNull(message = BIRTHDATE_CANNOT_BE_NULL) LocalDate birthdate,

                      @NotNull(message = ADDRESS_CANNOT_BE_NULL) @Valid AddressDTO address,

                      Date createdAt, Date updatedAt) {
}
