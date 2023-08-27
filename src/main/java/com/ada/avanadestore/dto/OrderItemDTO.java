package com.ada.avanadestore.dto;

import com.ada.avanadestore.dto.ProductDTO;

import java.util.UUID;

public record OrderItemDTO(UUID id, Integer quantity, ProductDTO product) {
}
