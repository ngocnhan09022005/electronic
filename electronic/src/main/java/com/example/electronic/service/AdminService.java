package com.example.electronic.service;

import com.example.electronic.dto.AdminDashboardDTO;
import com.example.electronic.dto.ClothingItemDTO;
import com.example.electronic.model.ClothingItem;
import com.example.electronic.model.OrderStatus;
import com.example.electronic.model.PaymentStatus;
import com.example.electronic.repository.ClothingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final ClothingRepository clothingRepository;
    private final CatalogService catalogService;
    private final OrderService orderService;

    public AdminService(ClothingRepository clothingRepository, CatalogService catalogService,
                       OrderService orderService) {
        this.clothingRepository = clothingRepository;
        this.catalogService = catalogService;
        this.orderService = orderService;
    }

    public AdminDashboardDTO getDashboard() {
        List<ClothingItem> items = clothingRepository.findAll();
        long total = items.size();
        long featured = items.stream().filter(ClothingItem::isFeatured).count();
        long outOfStock = items.stream().filter(item -> item.getStock() <= 0).count();
        List<ClothingItemDTO> latest = items.stream()
                .sorted(Comparator.comparing(ClothingItem::getId).reversed())
                .limit(5)
                .map(item -> catalogService.getItem(item.getId()))
                .collect(Collectors.toList());
        
        // Thống kê doanh thu
        BigDecimal totalRevenue = orderService.getTotalRevenue(PaymentStatus.COMPLETED);
        
        // Doanh thu hôm nay
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(23, 59, 59);
        BigDecimal todayRevenue = orderService.getTotalRevenueByDateRange(todayStart, todayEnd);
        
        // Doanh thu tháng này
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = LocalDate.now().atTime(23, 59, 59);
        BigDecimal monthRevenue = orderService.getTotalRevenueByDateRange(monthStart, monthEnd);
        
        // Thống kê đơn hàng
        long totalOrders = orderService.countOrdersByPaymentStatus(PaymentStatus.COMPLETED) +
                          orderService.countOrdersByPaymentStatus(PaymentStatus.PENDING);
        long pendingOrders = orderService.countOrdersByOrderStatus(OrderStatus.PENDING);
        long completedOrders = orderService.countOrdersByOrderStatus(OrderStatus.DELIVERED);
        long cancelledOrders = orderService.countOrdersByOrderStatus(OrderStatus.CANCELLED);
        
        return new AdminDashboardDTO(total, featured, outOfStock, latest, totalRevenue,
                                    todayRevenue, monthRevenue, totalOrders, pendingOrders,
                                    completedOrders, cancelledOrders);
    }
}

