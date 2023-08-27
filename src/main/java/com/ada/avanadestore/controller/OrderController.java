package com.ada.avanadestore.controller;

import com.ada.avanadestore.dto.CreateOrderDTO;
import com.ada.avanadestore.dto.CreateOrderItemDTO;
import com.ada.avanadestore.dto.OrderDTO;
import com.ada.avanadestore.dto.OrderFilterDTO;
import com.ada.avanadestore.enums.OrderStatus;
import com.ada.avanadestore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getByFilter(
            @RequestParam(required = false) UUID user,
            @RequestParam(required = false) LocalDate minDate,
            @RequestParam(required = false) LocalDate maxDate,
            @RequestParam(required = false) OrderStatus status
    ) {
        OrderFilterDTO filter = new OrderFilterDTO(user, minDate, maxDate, status);
        List<OrderDTO> orderDTOList = service.findByFilter(filter);
        return ResponseEntity.ok(orderDTOList);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody @Valid CreateOrderDTO dto) {
        OrderDTO createdOrder = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable UUID id, @RequestBody List<CreateOrderItemDTO> dto) {
        OrderDTO updatedOrder = service.update(id, dto);
        return ResponseEntity.ok(updatedOrder);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancel(@PathVariable UUID id) {
        OrderDTO cancelledOrder = service.cancel(id);
        return ResponseEntity.ok(cancelledOrder);
    }

    @PatchMapping("/{id}/finalize")
    public ResponseEntity<OrderDTO> finalize(@PathVariable UUID id) {
        OrderDTO finalizedOrder = service.finalize(id);
        return ResponseEntity.ok(finalizedOrder);
    }
}
