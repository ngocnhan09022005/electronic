package com.example.electronic.service;

import com.example.electronic.dto.CategoryDTO;
import com.example.electronic.dto.ClothingItemDTO;
import com.example.electronic.model.ClothingItem;
import com.example.electronic.model.ClothingType;
import com.example.electronic.repository.ClothingRepository;
import com.example.electronic.request.AddClothingRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final ClothingRepository clothingRepository;

    public CatalogService(ClothingRepository clothingRepository) {
        this.clothingRepository = clothingRepository;
    }

    public List<ClothingItemDTO> getAllItems() {
        return clothingRepository.findAll().stream()
                .sorted(Comparator.comparing(ClothingItem::getName))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ClothingItemDTO> getItemsByType(ClothingType type) {
        return clothingRepository.findByType(type).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ClothingItemDTO> getFeaturedItems() {
        return clothingRepository.findAll().stream()
                .filter(ClothingItem::isFeatured)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategories() {
        List<ClothingItem> all = clothingRepository.findAll();
        return Arrays.stream(ClothingType.values())
                .map(type -> new CategoryDTO(
                        type,
                        type.getDisplayName(),
                        getCategoryDescription(type),
                        all.stream().filter(item -> item.getType() == type).count()
                ))
                .collect(Collectors.toList());
    }

    public ClothingItemDTO createItem(AddClothingRequest request) {
        ClothingItem item = new ClothingItem(null, request.getName(), request.getDescription(),
                request.getPrice(), request.getType(), request.getImageUrl(), request.isFeatured(), request.getStock());
        return toDto(clothingRepository.save(item));
    }

    public ClothingItemDTO updateStock(Long itemId, int newStock) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        ClothingItem item = clothingRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id=" + itemId));
        item.setStock(newStock);
        return toDto(clothingRepository.save(item));
    }

    public ClothingItemDTO getItem(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        return clothingRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id=" + id));
    }

    /**
     * Cập nhật sản phẩm
     */
    public ClothingItemDTO updateItem(Long id, AddClothingRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        ClothingItem item = clothingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id=" + id));
        
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setType(request.getType());
        item.setImageUrl(request.getImageUrl());
        item.setFeatured(request.isFeatured());
        item.setStock(request.getStock());
        
        return toDto(clothingRepository.save(item));
    }

    /**
     * Xóa sản phẩm
     */
    public void deleteItem(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        if (!clothingRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm id=" + id);
        }
        clothingRepository.deleteById(id);
    }

    private ClothingItemDTO toDto(ClothingItem item) {
        return new ClothingItemDTO(item.getId(), item.getName(), item.getDescription(),
                item.getPrice(), item.getType(), item.getImageUrl(), item.isFeatured(), item.getStock());
    }

    private String getCategoryDescription(ClothingType type) {
        return switch (type) {
            case LONG_SLEEVE -> "Bộ sưu tập áo tay dài thanh lịch";
            case SHORT_SLEEVE -> "Áo phông mỏng nhẹ, phù hợp ngày hè";
            case LONG_PANTS -> "Quần dài văn phòng, jean, kaki";
            case SHORT_PANTS -> "Thoải mái năng động với các mẫu short";
            case OUTWEAR -> "Áo khoác, blazer tạo điểm nhấn";
            case ACCESSORY -> "Phụ kiện mix & match mọi outfit";
        };
    }
}

