package com.ada.avanadestore.service;

import com.ada.avanadestore.dto.*;
import com.ada.avanadestore.entitity.*;
import com.ada.avanadestore.enums.EmployeeRoles;
import com.ada.avanadestore.enums.OrderStatus;
import com.ada.avanadestore.event.EmailPublisher;
import com.ada.avanadestore.event.UpdateProductQuantityPublisher;
import com.ada.avanadestore.exception.BadRequestException;
import com.ada.avanadestore.exception.InternalServerException;
import com.ada.avanadestore.exception.ResourceNotFoundException;
import com.ada.avanadestore.repository.OrderFilterRepository;
import com.ada.avanadestore.repository.OrderRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final EmployeeService employeeService;

    public OrderService(OrderRepository repository, OrderFilterRepository filterRepository, ProductService productService, CustomerService customerService, UpdateProductQuantityPublisher productQuantityPublisher, EmailPublisher emailPublisher, EmployeeService employeeService) {
        this.repository = repository;
        this.filterRepository = filterRepository;
        this.productService = productService;
        this.customerService = customerService;
        this.productQuantityPublisher = productQuantityPublisher;
        this.emailPublisher = emailPublisher;
        this.employeeService = employeeService;
    }

    private Order getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND));
    }

    public List<OrderDTO> findByFilter(OrderFilterDTO dto) {
        return filterRepository.findByFilter(dto).stream().map(Order::toDTO).toList();
    }

    public OrderDTO create(CreateOrderDTO dto) {
        List<OrderItem> orderItemList = prepareOrderItems(dto.orderItems());
        Customer user = customerService.getById(dto.customerId());
        Order order = new Order(user, orderItemList);
        validateIfOrderItemsExceedAvailableProductsStock(order);
        order.setStatus(OrderStatus.CREATED);
        sendNotification(order);
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
        return repository.save(order).toDTO();
    }

    public OrderDTO setInProcess(UUID id) {
        return updateOrderStatus(id, IN_PROCESS);
    }

    public OrderDTO cancel(UUID id) {
        return updateOrderStatus(id, OrderStatus.CANCELLED);
    }

    public OrderDTO finalize(UUID id) {
        Order order = validateAndGetOrderById(id);
        validateIfOrderItemsExceedAvailableProductsStock(order);
        return updateOrderStatus(id, OrderStatus.COMPLETED, true);
    }

    private OrderDTO updateOrderStatus(UUID id, OrderStatus newStatus) {
        return updateOrderStatus(id, newStatus, false);
    }

    private OrderDTO updateOrderStatus(UUID id, OrderStatus newStatus, boolean shouldUpdateStock) {
        Order order = validateAndGetOrderById(id);

        if (shouldUpdateStock) {
            updateProductStock(order.getOrderItems());
        }

        order.setStatus(newStatus);
        trySaverOrThrowError(order);
        sendNotification(order);

        return order.toDTO();
    }

    private Order validateAndGetOrderById(UUID id) {
        Order order = getById(id);
        validateOrderStatus(order);
        return order;
    }

    private void validateOrderStatus(Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException("Order is either cancelled or completed.");
        }
    }

    private void sendNotification(Order order) {
        SalesEmailFormDTO emailForm = prepareEmailForm(order);
        emailPublisher.handleSendEmailEventSales(emailForm);
    }

    private void updateProductStock(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            UpdateProductQuantityDTO updateProductQuantityDTO =
                    new UpdateProductQuantityDTO(item.getProduct().getId(), item.getQuantity());
            productQuantityPublisher.handleUpdateProductQuantityEvent(updateProductQuantityDTO);
        }
    }

    private void trySaverOrThrowError(Order order) {
        try {
            repository.save(order);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(DATA_INTEGRITY_ERROR);
        } catch (Exception e) {
            throw new InternalServerException(INTERNAL_SERVER_ERROR);
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

    private SalesEmailFormDTO prepareEmailForm(Order order) {
        List<String> managerEmails = getManagerEmails();
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
        return new SalesEmailFormDTO(emailTo, managerEmails, subject, message);
    }

    private List<String> getManagerEmails() {
        List<Employee> managers = employeeService.getAllByRoleAndDepartmentName(EmployeeRoles.MANAGER, "SALES");
        return managers.stream().flatMap(manager -> Stream.of(manager.getEmail())).toList();
    }
}
