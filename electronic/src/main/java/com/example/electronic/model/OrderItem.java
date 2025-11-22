package com.example.electronic.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "clothing_item_id", nullable = false)
    private Long clothingItemId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothing_item_id", insertable = false, updatable = false)
    private ClothingItem clothingItem;

    public OrderItem() {
    }

    public OrderItem(Long orderId, Long clothingItemId, Integer quantity, BigDecimal price) {
        this.orderId = orderId;
        this.clothingItemId = clothingItemId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (subtotal == null && price != null && quantity != null) {
            subtotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getClothingItemId() {
        return clothingItemId;
    }

    public void setClothingItemId(Long clothingItemId) {
        this.clothingItemId = clothingItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        if (price != null) {
            this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        if (quantity != null) {
            this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ClothingItem getClothingItem() {
        return clothingItem;
    }

    public void setClothingItem(ClothingItem clothingItem) {
        this.clothingItem = clothingItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem orderItem)) return false;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

