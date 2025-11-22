package com.example.electronic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.electronic.model.ClothingType;
import com.example.electronic.service.CatalogService;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final CatalogService catalogService;

    public ShopController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping({"", "/"})
    public String rootRedirect() {
        // Redirect client to the explicit /shop/home path to avoid server-side relative forwards
        return "redirect:/shop/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        try {
        model.addAttribute("categories", catalogService.getCategories());
        model.addAttribute("featuredItems", catalogService.getFeaturedItems());
        } catch (Exception e) {
            // Nếu có lỗi khi lấy dữ liệu, trả về danh sách rỗng
            model.addAttribute("categories", java.util.Collections.emptyList());
            model.addAttribute("featuredItems", java.util.Collections.emptyList());
        }
        return "shop/home";
    }

    @GetMapping("/category/{slug}")
    public String byCategory(@PathVariable("slug") String slug, Model model) {
        ClothingType type = ClothingType.fromSlug(slug);
        model.addAttribute("category", type);
        model.addAttribute("items", catalogService.getItemsByType(type));
        return "shop/category";
    }
}

