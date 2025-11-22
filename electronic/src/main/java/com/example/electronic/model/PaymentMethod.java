package com.example.electronic.model;

public enum PaymentMethod {
    CREDIT_CARD("Thẻ tín dụng"),
    BANK_TRANSFER("Chuyển khoản ngân hàng"),
    COD("Thanh toán khi nhận"),
    E_WALLET("Ví điện tử");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

