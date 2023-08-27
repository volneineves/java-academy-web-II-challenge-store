package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.*;
import com.ada.avanadestore.entitity.Order;
import com.ada.avanadestore.entitity.OrderItem;
import com.ada.avanadestore.entitity.Product;
import com.ada.avanadestore.entitity.Customer;
import com.ada.avanadestore.enums.OrderStatus;
import com.ada.avanadestore.event.EmailPublisher;
import com.ada.avanadestore.event.UpdateProductQuantityPublisher;
import com.ada.avanadestore.exception.BadRequestException;
import com.ada.avanadestore.exception.ResourceNotFoundException;
import com.ada.avanadestore.repository.OrderFilterRepository;
import com.ada.avanadestore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ada.avanadestore.constants.Messages.*;
import static com.ada.avanadestore.enums.OrderStatus.IN_PROCESS;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderFilterRepository filterRepository;
    private final ProductService productService;
    private final CustomerService customerService;
    private final UpdateProductQuantityPublisher productQuantityPublisher;
    private final EmailPublisher emailPublisher;

    public OrderService(OrderRepository repository, OrderFilterRepository filterRepository, ProductService productService, CustomerService customerService, UpdateProductQuantityPublisher productQuantityPublisher, EmailPublisher emailPublisher) {
        this.repository = repository;
        this.filterRepository = filterRepository;
        this.productService = productService;
        this.customerService = customerService;
        this.productQuantityPublisher = productQuantityPublisher;
        this.emailPublisher = emailPublisher;
    }

    private Order getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND));
    }

    public List<OrderDTO> findByFilter(OrderFilterDTO dto) {
        return filterRepository.findByFilter(dto).stream().map(Order::toDTO).toList();
    }

    public OrderDTO create(CreateOrderDTO dto) {
        List<OrderItem> orderItemList = prepareOrderItems(dto.orderItems());
        Customer user = customerService.getById(dto.user());
        Order order = new Order(user, orderItemList);
        validateIfOrderItemsExceedAvailableProductsStock(order);
        order.setStatus(OrderStatus.CREATED);
        sendEmailByOrder(order);
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
        order.setStatus(IN_PROCESS); // TODO adicionar usuario funcionario para permitir order
        sendEmailByOrder(order);
        return repository.save(order).toDTO();
    }

    public OrderDTO cancel(UUID id) {
        Order order = getById(id);
        switch (order.getStatus()) {
            case CANCELLED -> throw new BadRequestException(ORDER_CANCELLED);
            case COMPLETED -> throw new BadRequestException(ORDER_COMPLETED);
            default -> {
                order.setStatus(OrderStatus.CANCELLED);
                sendEmailByOrder(order);
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
                sendEmailByOrder(order);
                return repository.save(order).toDTO();
            }
        }
    }

    private void sendEventToUpdateProductStock(List<OrderItem> orderItems) {

        for (OrderItem orderItem : orderItems) {
            UUID productId = orderItem.getProduct().getId();
            int quantity = orderItem.getQuantity();
            UpdateProductQuantityDTO updateProductQuantityDTO = new UpdateProductQuantityDTO(productId, quantity);
            productQuantityPublisher.handleUpdateProductQuantityEvent(updateProductQuantityDTO);
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

    private void sendEmailByOrder(Order order) {
        EmailFormDTO emailForm = createEmailForm(order);
        emailPublisher.handleSendEmailEvent(emailForm);
    }

    private EmailFormDTO createEmailForm(Order order) {
        String emailTo = order.getCustomer().getEmail();
        String subject = SUBJECT_ORDER_UNKNOWN;
        String message = MESSAGE_ORDER_UNKNOWN;

        switch (order.getStatus()) {
            case CREATED:
                subject = SUBJECT_ORDER_CREATED;
                message = MESSAGE_ORDER_CREATED;
                break;
            case IN_PROCESS:
                subject = SUBJECT_ORDER_IN_PROCESS;
                message = MESSAGE_ORDER_IN_PROCESS;
                break;
            case COMPLETED:
                subject = SUBJECT_ORDER_COMPLETED;
                message = MESSAGE_ORDER_COMPLETED;
                break;
            case CANCELLED:
                subject = SUBJECT_ORDER_CANCELLED;
                message = MESSAGE_ORDER_CANCELLED;
        }
        return new EmailFormDTO(emailTo, subject, message);
    }
}
