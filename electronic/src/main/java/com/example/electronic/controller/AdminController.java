package com.example.electronic.controller;

import com.example.electronic.dto.AdminDashboardDTO;
import com.example.electronic.dto.ClothingItemDTO;
import com.example.electronic.request.AddClothingRequest;
import com.example.electronic.request.UpdateStockRequest;
import com.example.electronic.service.AdminService;
import com.example.electronic.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final CatalogService catalogService;

    public AdminController(AdminService adminService, CatalogService catalogService) {
        this.adminService = adminService;
        this.catalogService = catalogService;
    }

    @GetMapping("/dashboard")
    public AdminDashboardDTO dashboard() {
        return adminService.getDashboard();
    }

    @PostMapping("/items")
    public ClothingItemDTO createItem(@Valid @RequestBody AddClothingRequest request) {
        return catalogService.createItem(request);
    }

    @PatchMapping("/items/stock")
    public ClothingItemDTO updateStock(@Valid @RequestBody UpdateStockRequest request) {
        return catalogService.updateStock(request.getItemId(), request.getNewStock());
    }
}

