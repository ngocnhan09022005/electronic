package com.example.electronic.controller;

import com.example.electronic.dto.ClothingItemDTO;
import com.example.electronic.model.*;
import com.example.electronic.request.AddClothingRequest;
import com.example.electronic.request.UpdateStockRequest;
import com.example.electronic.service.AdminService;
import com.example.electronic.service.CatalogService;
import com.example.electronic.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final AdminService adminService;
    private final CatalogService catalogService;
    private final OrderService orderService;

    public AdminPageController(AdminService adminService, CatalogService catalogService,
                              OrderService orderService) {
        this.adminService = adminService;
        this.catalogService = catalogService;
        this.orderService = orderService;
    }

    /**
     * Dashboard - Trang tổng quan
     */
    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("dashboard", adminService.getDashboard());
        return "admin/dashboard";
    }

    /**
     * Quản lý sản phẩm - Danh sách sản phẩm
     */
    @GetMapping("/products")
    public String products(Model model) {
        List<ClothingItemDTO> items = catalogService.getAllItems();
        model.addAttribute("items", items);
        model.addAttribute("types", ClothingType.values());
        return "admin/products";
    }

    /**
     * Form thêm sản phẩm
     */
    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("item", new AddClothingRequest());
        model.addAttribute("types", ClothingType.values());
        return "admin/product-form";
    }

    /**
     * Xử lý thêm sản phẩm
     */
    @PostMapping("/products/add")
    public String addProduct(@Valid @ModelAttribute("item") AddClothingRequest request,
                            BindingResult bindingResult, Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", ClothingType.values());
            return "admin/product-form";
        }
        try {
            catalogService.createItem(request);
            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    /**
     * Form sửa sản phẩm
     */
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        ClothingItemDTO item = catalogService.getItem(id);
        AddClothingRequest request = new AddClothingRequest();
        request.setName(item.getName());
        request.setDescription(item.getDescription());
        request.setPrice(item.getPrice());
        request.setType(item.getType());
        request.setImageUrl(item.getImageUrl());
        request.setFeatured(item.isFeatured());
        request.setStock(item.getStock());
        model.addAttribute("item", request);
        model.addAttribute("itemId", id);
        model.addAttribute("types", ClothingType.values());
        return "admin/product-form";
    }

    /**
     * Xử lý cập nhật sản phẩm
     */
    @PostMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                               @Valid @ModelAttribute("item") AddClothingRequest request,
                               BindingResult bindingResult, Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("itemId", id);
            model.addAttribute("types", ClothingType.values());
            return "admin/product-form";
        }
        try {
            catalogService.updateItem(id, request);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    /**
     * Xóa sản phẩm
     */
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            catalogService.deleteItem(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    /**
     * Quản lý đơn hàng - Danh sách đơn hàng
     */
    @GetMapping("/orders")
    public String orders(Model model, @RequestParam(required = false) String status) {
        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                orders = orderService.getOrdersByStatus(orderStatus);
            } catch (IllegalArgumentException e) {
                orders = orderService.getAllOrders();
            }
        } else {
            orders = orderService.getAllOrders();
        }
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("paymentStatuses", PaymentStatus.values());
        model.addAttribute("selectedStatus", status);
        return "admin/orders";
    }

    /**
     * Chi tiết đơn hàng
     */
    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng id=" + id));
        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("paymentStatuses", PaymentStatus.values());
        return "admin/order-detail";
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                   @RequestParam OrderStatus status,
                                   RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    /**
     * Cập nhật trạng thái thanh toán
     */
    @PostMapping("/orders/{id}/payment-status")
    public String updatePaymentStatus(@PathVariable Long id,
                                     @RequestParam PaymentStatus status,
                                     RedirectAttributes redirectAttributes) {
        try {
            orderService.updatePaymentStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thanh toán thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    /**
     * Nhập kho - Form nhập kho
     */
    @GetMapping("/inventory")
    public String inventory(Model model) {
        List<ClothingItemDTO> items = catalogService.getAllItems();
        model.addAttribute("items", items);
        model.addAttribute("stockRequest", new UpdateStockRequest());
        return "admin/inventory";
    }

    /**
     * Xử lý nhập kho
     */
    @PostMapping("/inventory/add")
    public String addStock(@Valid @ModelAttribute("stockRequest") UpdateStockRequest request,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Dữ liệu không hợp lệ!");
            return "redirect:/admin/inventory";
        }
        try {
            orderService.addStock(request.getItemId(), request.getNewStock());
            redirectAttributes.addFlashAttribute("success", 
                "Nhập kho thành công! Đã thêm " + request.getNewStock() + " sản phẩm.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/inventory";
    }
}

