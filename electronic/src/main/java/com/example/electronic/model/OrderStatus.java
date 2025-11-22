package com.example.electronic.model;

public enum OrderStatus {
    PENDING("Chờ xử lý"),
    PROCESSING("Đang xử lý"),
    SHIPPED("Đã gửi hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Đã hủy");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

