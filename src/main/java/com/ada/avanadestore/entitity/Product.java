package com.ada.avanadestore.entitity;

import com.ada.avanadestore.dto.ProductDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 40)
    private String title;

    @Column
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    private Double discountPercentage;

    private Double rating;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 40)
    private String brand;

    @Column(length = 40)
    private String category;

    @Column(length = 255)
    private String thumbnail;

    @ElementCollection
    private Set<String> images = new HashSet<>();

    public Product() {
    }

    public Product(ProductDTO dto) {
        this.title = dto.title();
        this.description = dto.description();
        this.price = dto.price();
        this.discountPercentage = dto.discountPercentage();
        this.rating = dto.rating();
        this.stock = dto.stock();
        this.brand = dto.brand();
        this.category = dto.category();
        this.thumbnail = dto.thumbnail();
        this.images = dto.images();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    public ProductDTO toDTO() {
        return new ProductDTO(
                id,
                title,
                description,
                price,
                discountPercentage,
                rating,
                stock,
                brand,
                category,
                thumbnail,
                images
        );
    }
}
