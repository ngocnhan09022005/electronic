package com.example.electronic.model;

public class ClothingCategory {

    private ClothingType type;
    private String description;

    public ClothingCategory(ClothingType type, String description) {
        this.type = type;
        this.description = description;
    }

    public ClothingType getType() {
        return type;
    }

    public void setType(ClothingType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

