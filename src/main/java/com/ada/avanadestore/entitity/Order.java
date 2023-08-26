package com.ada.avanadestore.entitity;

import com.ada.avanadestore.enums.OrderStatus;
import com.ada.avanadestore.service.OrderDTO;
import com.ada.avanadestore.service.OrderItemDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
    }

    public Order(User user, List<OrderItem> orderItems) {
        this.orderDate = new Date();
        this.user = user;
        this.orderItems = orderItems;
    }

    public UUID getId() {
        return id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> itemOrders) {
        this.orderItems = itemOrders;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return orderItems.stream()
                .map(orderItem -> {
                    BigDecimal price = orderItem.getProduct().getPrice();
                    BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());
                    return quantity.multiply(price).setScale(2, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalWithDiscounts() {
        return orderItems.stream()
                .map(orderItem -> {
                    BigDecimal price = orderItem.getProduct().getPrice();
                    BigDecimal quantity = BigDecimal.valueOf(orderItem.getQuantity());
                    BigDecimal totalForItem = quantity.multiply(price);

                    BigDecimal discountPercentage = BigDecimal.valueOf(orderItem.getProduct().getDiscountPercentage());
                    BigDecimal discount = totalForItem.multiply(discountPercentage).divide(BigDecimal.valueOf(100));

                    return totalForItem.subtract(discount).setScale(2, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public OrderDTO toDTO() {
        List<OrderItemDTO> orderItemDTOList = orderItems.stream().map(OrderItem::toDTO).toList();
        return new OrderDTO(id, orderDate, status, this.getTotal(), this.getTotalWithDiscounts(), orderItemDTOList);
    }
}
