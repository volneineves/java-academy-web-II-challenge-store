package com.ada.avanadestore.dto;

import java.util.UUID;

public record CreateOrderItemDTO(Integer quantity, UUID product) {
}
