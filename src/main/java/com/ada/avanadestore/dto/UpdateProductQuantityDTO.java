package com.ada.avanadestore.dto;

import java.util.UUID;

public record UpdateProductQuantityDTO(UUID id, Integer quantityDecrease) {
}
