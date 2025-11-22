package com.example.electronic.dto;

import java.math.BigDecimal;
import java.util.List;

public class AdminDashboardDTO {

    private long totalProducts;
    private long featuredProducts;
    private long outOfStockProducts;
    private List<ClothingItemDTO> latestProducts;
    
    // Thống kê doanh thu
    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private BigDecimal monthRevenue;
    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
    private long cancelledOrders;

    public AdminDashboardDTO(long totalProducts, long featuredProducts, long outOfStockProducts,
                             List<ClothingItemDTO> latestProducts) {
        this.totalProducts = totalProducts;
        this.featuredProducts = featuredProducts;
        this.outOfStockProducts = outOfStockProducts;
        this.latestProducts = latestProducts;
    }

    public AdminDashboardDTO(long totalProducts, long featuredProducts, long outOfStockProducts,
                             List<ClothingItemDTO> latestProducts, BigDecimal totalRevenue,
                             BigDecimal todayRevenue, BigDecimal monthRevenue, long totalOrders,
                             long pendingOrders, long completedOrders, long cancelledOrders) {
        this.totalProducts = totalProducts;
        this.featuredProducts = featuredProducts;
        this.outOfStockProducts = outOfStockProducts;
        this.latestProducts = latestProducts;
        this.totalRevenue = totalRevenue;
        this.todayRevenue = todayRevenue;
        this.monthRevenue = monthRevenue;
        this.totalOrders = totalOrders;
        this.pendingOrders = pendingOrders;
        this.completedOrders = completedOrders;
        this.cancelledOrders = cancelledOrders;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public long getFeaturedProducts() {
        return featuredProducts;
    }

    public long getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public List<ClothingItemDTO> getLatestProducts() {
        return latestProducts;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
    }

    public BigDecimal getTodayRevenue() {
        return todayRevenue != null ? todayRevenue : BigDecimal.ZERO;
    }

    public BigDecimal getMonthRevenue() {
        return monthRevenue != null ? monthRevenue : BigDecimal.ZERO;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    public long getCancelledOrders() {
        return cancelledOrders;
    }
}

