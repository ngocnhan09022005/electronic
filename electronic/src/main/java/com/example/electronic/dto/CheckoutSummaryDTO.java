package com.example.electronic.dto;

import com.example.electronic.model.PaymentMethod;
import com.example.electronic.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CheckoutSummaryDTO {

    private String orderCode;
    private BigDecimal totalAmount;
    private PaymentStatus status;
    private PaymentMethod method;
    private LocalDateTime createdAt;
    private List<ClothingItemDTO> items;

    public CheckoutSummaryDTO(String orderCode, BigDecimal totalAmount, PaymentStatus status,
                              PaymentMethod method, LocalDateTime createdAt, List<ClothingItemDTO> items) {
        this.orderCode = orderCode;
        this.totalAmount = totalAmount;
        this.status = status;
        this.method = method;
        this.createdAt = createdAt;
        this.items = items;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<ClothingItemDTO> getItems() {
        return items;
    }
}

