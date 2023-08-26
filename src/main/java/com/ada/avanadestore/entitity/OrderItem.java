package com.ada.avanadestore.entitity;

import com.ada.avanadestore.dto.CreateOrderItemDTO;
import com.ada.avanadestore.service.OrderItemDTO;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public OrderItem() {
    }

    public OrderItem(CreateOrderItemDTO dto, Product product) {
        this.quantity = dto.quantity();
        this.product = product;
    }


    public UUID getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderItemDTO toDTO() {
        return new OrderItemDTO(id, quantity, product.toDTO());
    }
}
