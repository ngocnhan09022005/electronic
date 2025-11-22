package com.example.electronic.dto;

import com.example.electronic.model.ClothingType;

import java.math.BigDecimal;

public class ClothingItemDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ClothingType type;
    private String imageUrl;
    private boolean featured;
    private int stock;

    public ClothingItemDTO() {
    }

    public ClothingItemDTO(Long id, String name, String description, BigDecimal price,
                           ClothingType type, String imageUrl, boolean featured, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ClothingType getType() {
        return type;
    }

    public void setType(ClothingType type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

