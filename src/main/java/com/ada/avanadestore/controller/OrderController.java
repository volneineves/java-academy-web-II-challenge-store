package com.ada.avanadestore.controller;

import com.ada.avanadestore.dto.CreateOrderDTO;
import com.ada.avanadestore.service.OrderDTO;
import com.ada.avanadestore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody CreateOrderDTO dto) {
        OrderDTO createdOrder = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
}
