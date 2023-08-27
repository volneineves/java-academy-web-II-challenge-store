package com.ada.avanadestore.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import static com.ada.avanadestore.constants.Messages.*;

public record CreateOrderDTO(@NotNull(message = USER_NOT_NULL) UUID user,
                             @NotNull(message = ORDER_ITEMS_NOT_NULL) @NotEmpty(message = ORDER_ITEMS_NOT_EMPTY) @Valid List<CreateOrderItemDTO> orderItems) {
}
