package com.ada.avanadestore.dto;

import java.util.UUID;

public record AddressDTO(UUID id, String street, String number, String city, String state, String zip, String complement) {
}
