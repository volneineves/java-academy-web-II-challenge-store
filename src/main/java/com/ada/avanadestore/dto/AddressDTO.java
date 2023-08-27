package com.ada.avanadestore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.*;
import static com.ada.avanadestore.constants.RegexPatterns.ZIP_FORMAT;

public record AddressDTO(UUID id,

                         @NotNull(message = STREET_CANNOT_BE_NULL) @Size(max = 80, message = STREET_SIZE_EXCEEDED) String street,

                         @NotNull(message = NUMBER_CANNOT_BE_NULL) @Size(max = 10, message = NUMBER_SIZE_EXCEEDED) String number,

                         @NotNull(message = CITY_CANNOT_BE_NULL) @Size(max = 60, message = CITY_SIZE_EXCEEDED) String city,

                         @NotNull(message = STATE_CANNOT_BE_NULL) @Size(max = 60, message = STATE_SIZE_EXCEEDED) String state,

                         @NotNull(message = ZIP_CANNOT_BE_NULL) @Size(max = 9, message = ZIP_SIZE_EXCEEDED) @Pattern(regexp = ZIP_FORMAT, message = ZIP_INVALID_FORMAT) String zip,

                         String complement) {
}
