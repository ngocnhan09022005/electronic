package com.example.electronic.controller;

import com.example.electronic.dto.CategoryDTO;
import com.example.electronic.dto.ClothingItemDTO;
import com.example.electronic.model.ClothingType;
import com.example.electronic.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopRestController {

    private final CatalogService catalogService;

    public ShopRestController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/items")
    public List<ClothingItemDTO> getItems(@RequestParam(value = "type", required = false) String type) {
        if (type == null) {
            return catalogService.getAllItems();
        }
        ClothingType clothingType = ClothingType.fromSlug(type);
        return catalogService.getItemsByType(clothingType);
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getCategories() {
        return catalogService.getCategories();
    }
}

