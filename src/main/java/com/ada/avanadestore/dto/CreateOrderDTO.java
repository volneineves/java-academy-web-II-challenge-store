package com.ada.avanadestore.dto;

import java.util.List;
import java.util.UUID;

public record CreateOrderDTO(UUID user, List<CreateOrderItemDTO> orderItems) {
}
