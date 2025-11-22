package com.example.electronic.model;

import java.util.Arrays;

public enum ClothingType {
    LONG_SLEEVE("ao-tay-dai", "Áo tay dài"),
    SHORT_SLEEVE("ao-tay-ngan", "Áo tay ngắn"),
    LONG_PANTS("quan-dai", "Quần dài"),
    SHORT_PANTS("quan-ngan", "Quần ngắn"),
    OUTWEAR("ao-khoac", "Áo khoác"),
    ACCESSORY("phu-kien", "Phụ kiện");

    private final String slug;
    private final String displayName;

    ClothingType(String slug, String displayName) {
        this.slug = slug;
        this.displayName = displayName;
    }

    public String getSlug() {
        return slug;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ClothingType fromSlug(String value) {
        return Arrays.stream(values())
                .filter(type -> type.slug.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại quần áo: " + value));
    }
}

