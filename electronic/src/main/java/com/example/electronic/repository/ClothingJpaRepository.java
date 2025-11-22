package com.example.electronic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.electronic.model.ClothingItem;
import com.example.electronic.model.ClothingType;

public interface ClothingJpaRepository extends JpaRepository<ClothingItem, Long> {
    List<ClothingItem> findByType(ClothingType type);
}
