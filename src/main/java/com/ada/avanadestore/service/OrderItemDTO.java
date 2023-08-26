package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.ProductDTO;

import java.util.UUID;

public record OrderItemDTO(UUID id, Integer quantity, ProductDTO product) {
}
