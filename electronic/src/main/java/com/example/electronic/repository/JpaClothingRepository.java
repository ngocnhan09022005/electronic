package com.example.electronic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.electronic.model.ClothingItem;
import com.example.electronic.model.ClothingType;

@Primary
@Repository
public class JpaClothingRepository implements ClothingRepository {

    private final ClothingJpaRepository jpa;

    public JpaClothingRepository(ClothingJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<ClothingItem> findAll() {
        return jpa.findAll();
    }

    @Override
    public List<ClothingItem> findByType(ClothingType type) {
        return jpa.findByType(type);
    }

    @Override
    public Optional<ClothingItem> findById(@NonNull Long id) {
        return jpa.findById(id);
    }

    @Override
    @NonNull
    public ClothingItem save(@NonNull ClothingItem item) {
        return jpa.save(item);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        jpa.deleteById(id);
    }
}
