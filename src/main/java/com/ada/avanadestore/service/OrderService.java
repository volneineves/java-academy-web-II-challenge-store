package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.*;
import com.ada.avanadestore.entitity.Order;
import com.ada.avanadestore.entitity.OrderItem;
import com.ada.avanadestore.entitity.Product;
import com.ada.avanadestore.entitity.User;
import com.ada.avanadestore.enums.OrderStatus;
import com.ada.avanadestore.event.UpdateProductQuantityPublisher;
import com.ada.avanadestore.exception.BadRequestException;
import com.ada.avanadestore.exception.ResourceNotFoundException;
import com.ada.avanadestore.repository.OrderFilterRepository;
import com.ada.avanadestore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ada.avanadestore.constants.ErrorMessages.*;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderFilterRepository filterRepository;
    private final ProductService productService;
    private final UserService userService;
    private final UpdateProductQuantityPublisher productQuantityPublisher;

    public OrderService(OrderRepository repository, OrderFilterRepository filterRepository, ProductService productService, UserService userService, UpdateProductQuantityPublisher productQuantityPublisher) {
        this.repository = repository;
        this.filterRepository = filterRepository;
        this.productService = productService;
        this.userService = userService;
        this.productQuantityPublisher = productQuantityPublisher;
    }

    private Order getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND));
    }

    public List<OrderDTO> findByFilter(OrderFilterDTO dto) {
        return filterRepository.findByFilter(dto).stream().map(Order::toDTO).toList();
    }

    public OrderDTO create(CreateOrderDTO dto) {
        List<OrderItem> orderItemList = prepareOrderItems(dto.orderItems());
        User user = userService.getById(dto.user());
        Order order = new Order(user, orderItemList);
        validateIfOrderItemsExceedAvailableProductsStock(order);
        order.setStatus(OrderStatus.CREATED);
        return repository.save(order).toDTO();
    }

    public OrderDTO update(UUID id, List<CreateOrderItemDTO> orderItemDTOList) {
        Order order = getById(id);
        List<OrderItem> orderItemList = switch (order.getStatus()) {
            case CANCELLED -> throw new BadRequestException(ORDER_CANCELLED);
            case COMPLETED -> throw new BadRequestException(ORDER_COMPLETED);
            default -> prepareOrderItems(orderItemDTOList);
        };

        order.setOrderItems(orderItemList);
        validateIfOrderItemsExceedAvailableProductsStock(order);
        order.setStatus(OrderStatus.IN_PROCESS);
        return repository.save(order).toDTO();
    }

    public OrderDTO cancel(UUID id) {
        Order order = getById(id);
        switch (order.getStatus()) {
            case CANCELLED -> throw new BadRequestException(ORDER_CANCELLED);
            case COMPLETED -> throw new BadRequestException(ORDER_COMPLETED);
            default -> {
                order.setStatus(OrderStatus.CANCELLED);
                return repository.save(order).toDTO();
            }
        }
    }

    public OrderDTO finalize(UUID id) {
        Order order = getById(id);
        switch (order.getStatus()) {
            case CANCELLED -> throw new BadRequestException(ORDER_CANCELLED);
            case COMPLETED -> throw new BadRequestException(ORDER_COMPLETED);
            default -> {
                validateIfOrderItemsExceedAvailableProductsStock(order);
                order.setStatus(OrderStatus.COMPLETED);
                sendEventToUpdateProductStock(order.getOrderItems());
                return repository.save(order).toDTO();
            }
        }
    }

    private void sendEventToUpdateProductStock(List<OrderItem> orderItems) {

        for (OrderItem orderItem : orderItems) {
            UUID productId = orderItem.getProduct().getId();
            int quantity = orderItem.getQuantity();
            UpdateProductQuantityDTO updateProductQuantityDTO = new UpdateProductQuantityDTO(productId, quantity);
            productQuantityPublisher.updateOder(updateProductQuantityDTO);
        }
    }

    public void validateIfOrderItemsExceedAvailableProductsStock(Order order) {
        Set<Product> products = order.getOrderItems().stream()
                .map(OrderItem::getProduct)
                .collect(Collectors.toSet());

        products.forEach(product -> {
            int totalOrderItemsQuantityByProduct = order.getOrderItems().stream()
                    .filter(orderItem -> orderItem.getProduct().getId().equals(product.getId()))
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
            if (totalOrderItemsQuantityByProduct > product.getStock()) {
                throw new BadRequestException(EXCEED_PRODUCT_STOCK);
            }
        });
    }

    private List<OrderItem> prepareOrderItems(List<CreateOrderItemDTO> orderItemDTOList) {
        Map<UUID, OrderItem> orderItemMap = new HashMap<>();

        for (CreateOrderItemDTO orderItemDTO : orderItemDTOList) {

            UUID productId = orderItemDTO.product();
            Product product = productService.getById(productId);

            boolean isProductAlreadyExists = orderItemMap.containsKey(productId);

            if (isProductAlreadyExists) {
                OrderItem existingOrderItem = orderItemMap.get(productId);
                int updatedQuantity = existingOrderItem.getQuantity() + orderItemDTO.quantity();
                existingOrderItem.setQuantity(updatedQuantity);
            } else {
                OrderItem newOrderItem = new OrderItem(orderItemDTO, product);
                orderItemMap.put(productId, newOrderItem);
            }
        }

        return new ArrayList<>(orderItemMap.values());
    }
}
