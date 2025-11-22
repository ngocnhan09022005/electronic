package com.example.electronic.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateStockRequest {

    @NotNull
    private Long itemId;
    @Min(0)
    private int newStock;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getNewStock() {
        return newStock;
    }

    public void setNewStock(int newStock) {
        this.newStock = newStock;
    }
}

