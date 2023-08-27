package com.ada.avanadestore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.*;

public record CreateOrderItemDTO(
        @NotNull(message = QUANTITY_NOT_NULL) @Min(value = 1, message = QUANTITY_MIN_VALUE) Integer quantity,
        @NotNull(message = PRODUCT_NOT_NULL) UUID product) {
}
