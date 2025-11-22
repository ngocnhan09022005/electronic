package com.example.electronic.repository;

import com.example.electronic.model.Order;
import com.example.electronic.model.OrderStatus;
import com.example.electronic.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderCode(String orderCode);
    
    List<Order> findByUserId(Long userId);
    
    List<Order> findByOrderStatus(OrderStatus status);
    
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate")
    List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.paymentStatus = :status")
    java.math.BigDecimal getTotalRevenueByPaymentStatus(PaymentStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = :status")
    Long countByPaymentStatus(PaymentStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = :status")
    Long countByOrderStatus(OrderStatus status);
}

