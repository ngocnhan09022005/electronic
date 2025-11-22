package com.example.electronic.service;

import com.example.electronic.model.*;
import com.example.electronic.repository.ClothingRepository;
import com.example.electronic.repository.OrderItemRepository;
import com.example.electronic.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ClothingRepository clothingRepository;

    public OrderService(OrderRepository orderRepository, 
                       OrderItemRepository orderItemRepository,
                       ClothingRepository clothingRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.clothingRepository = clothingRepository;
    }

    /**
     * Tạo đơn hàng mới và tự động trừ kho
     */
    @Transactional
    public Order createOrder(String orderCode, Long userId, BigDecimal totalAmount,
                            PaymentMethod paymentMethod, PaymentStatus paymentStatus,
                            OrderStatus orderStatus, String shippingAddress,
                            String shippingPhone, String shippingName, String notes,
                            List<OrderItemData> items) {
        // Tạo đơn hàng
        Order order = new Order(orderCode, userId, totalAmount, paymentMethod, 
                               paymentStatus, orderStatus);
        order.setShippingAddress(shippingAddress);
        order.setShippingPhone(shippingPhone);
        order.setShippingName(shippingName);
        order.setNotes(notes);
        order = orderRepository.save(order);

        // Tạo order items và trừ kho
        for (OrderItemData itemData : items) {
            ClothingItem item = clothingRepository.findById(itemData.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id=" + itemData.getItemId()));
            
            // Kiểm tra số lượng tồn kho
            if (item.getStock() < itemData.getQuantity()) {
                throw new IllegalArgumentException("Không đủ hàng trong kho cho sản phẩm: " + item.getName());
            }
            
            // Trừ kho
            item.setStock(item.getStock() - itemData.getQuantity());
            clothingRepository.save(item);
            
            // Tạo order item
            OrderItem orderItem = new OrderItem(order.getId(), itemData.getItemId(),
                                               itemData.getQuantity(), itemData.getPrice());
            orderItemRepository.save(orderItem);
        }

        return order;
    }

    /**
     * Nhập kho - thêm số lượng vào sản phẩm
     */
    @Transactional
    public ClothingItem addStock(Long itemId, int quantity) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng nhập kho phải lớn hơn 0");
        }
        
        ClothingItem item = clothingRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id=" + itemId));
        
        item.setStock(item.getStock() + quantity);
        return clothingRepository.save(item);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng id=" + orderId));
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    /**
     * Lưu đơn hàng (cập nhật)
     */
    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    /**
     * Cập nhật trạng thái thanh toán
     */
    @Transactional
    public Order updatePaymentStatus(Long orderId, PaymentStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng id=" + orderId));
        order.setPaymentStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByOrderStatus(status);
    }

    public Optional<Order> getOrderByCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * Tính tổng doanh thu theo trạng thái thanh toán
     */
    public BigDecimal getTotalRevenue(PaymentStatus paymentStatus) {
        BigDecimal total = orderRepository.getTotalRevenueByPaymentStatus(paymentStatus);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Tính tổng doanh thu trong khoảng thời gian
     */
    public BigDecimal getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findByDateRange(startDate, endDate);
        return orders.stream()
                .filter(o -> o.getPaymentStatus() == PaymentStatus.COMPLETED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Đếm số đơn hàng theo trạng thái thanh toán
     */
    public Long countOrdersByPaymentStatus(PaymentStatus paymentStatus) {
        return orderRepository.countByPaymentStatus(paymentStatus);
    }

    /**
     * Đếm số đơn hàng theo trạng thái đơn hàng
     */
    public Long countOrdersByOrderStatus(OrderStatus orderStatus) {
        return orderRepository.countByOrderStatus(orderStatus);
    }

    /**
     * Inner class để chứa dữ liệu order item
     */
    public static class OrderItemData {
        private Long itemId;
        private Integer quantity;
        private BigDecimal price;

        public OrderItemData(Long itemId, Integer quantity, BigDecimal price) {
            this.itemId = itemId;
            this.quantity = quantity;
            this.price = price;
        }

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}

