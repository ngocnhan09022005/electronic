package com.example.electronic.dto;

import com.example.electronic.model.ClothingType;

public class CategoryDTO {

    private ClothingType type;
    private String displayName;
    private String description;
    private long itemCount;

    public CategoryDTO(ClothingType type, String displayName, String description, long itemCount) {
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.itemCount = itemCount;
    }

    public ClothingType getType() {
        return type;
    }

    public void setType(ClothingType type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getItemCount() {
        return itemCount;
    }

    public void setItemCount(long itemCount) {
        this.itemCount = itemCount;
    }
}

