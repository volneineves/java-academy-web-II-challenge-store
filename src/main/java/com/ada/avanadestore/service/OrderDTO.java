package com.ada.avanadestore.service;

import com.ada.avanadestore.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record OrderDTO(UUID id, Date orderDate, OrderStatus status, BigDecimal total, BigDecimal totalWithDiscounts, List<OrderItemDTO> orderItems) {
}
