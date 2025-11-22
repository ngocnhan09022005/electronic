package com.example.electronic.repository;

import com.example.electronic.model.ClothingItem;
import com.example.electronic.model.ClothingType;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

public interface ClothingRepository {

    List<ClothingItem> findAll();

    List<ClothingItem> findByType(ClothingType type);

    Optional<ClothingItem> findById(@NonNull Long id);

    @NonNull
    ClothingItem save(@NonNull ClothingItem item);

    void deleteById(@NonNull Long id);
}

