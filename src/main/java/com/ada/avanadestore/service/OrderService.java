package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.CreateOrderDTO;
import com.ada.avanadestore.dto.CreateOrderItemDTO;
import com.ada.avanadestore.dto.OrderDTO;
import com.ada.avanadestore.entitity.Order;
import com.ada.avanadestore.entitity.OrderItem;
import com.ada.avanadestore.entitity.Product;
import com.ada.avanadestore.entitity.User;
import com.ada.avanadestore.enums.OrderStatus;
import com.ada.avanadestore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductService productService;
    private final UserService userService;

    public OrderService(OrderRepository repository, ProductService productService, UserService userService) {
        this.repository = repository;
        this.productService = productService;
        this.userService = userService;
    }

    public OrderDTO create(CreateOrderDTO dto) {
        List<OrderItem> orderItemList = prepareOrderItems(dto.orderItems());
        User user = userService.getById(dto.user());
        Order order = new Order(user, orderItemList);
        setOrderStatus(order, OrderStatus.CREATED);
        return repository.save(order).toDTO();
    }

    private List<OrderItem> prepareOrderItems(List<CreateOrderItemDTO> orderItemDTOList) {
        return orderItemDTOList.stream().map(orderItem -> {
            Product product = productService.getById(orderItem.product());
            return new OrderItem(orderItem, product);
        }).toList();
    }
    private static void setOrderStatus(Order order, OrderStatus status) {
        order.setStatus(status);
    }

}
