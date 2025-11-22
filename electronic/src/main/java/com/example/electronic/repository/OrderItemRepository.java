package com.example.electronic.repository;

import com.example.electronic.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrderId(Long orderId);
    
    List<OrderItem> findByClothingItemId(Long clothingItemId);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderId IN (SELECT o.id FROM Order o WHERE o.paymentStatus = com.example.electronic.model.PaymentStatus.COMPLETED)")
    List<OrderItem> findByPaidOrders();
}

