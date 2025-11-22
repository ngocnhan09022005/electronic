package com.example.electronic.service;

import com.example.electronic.dto.CheckoutSummaryDTO;
import com.example.electronic.model.*;
import com.example.electronic.repository.ClothingRepository;
import com.example.electronic.request.CheckoutRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private final ClothingRepository clothingRepository;
    private final CatalogService catalogService;
    private final OrderService orderService;
    private final Random random = new Random();

    public CheckoutService(ClothingRepository clothingRepository, CatalogService catalogService,
                          OrderService orderService) {
        this.clothingRepository = clothingRepository;
        this.catalogService = catalogService;
        this.orderService = orderService;
    }

    /**
     * Checkout và lưu đơn hàng vào database, tự động trừ kho
     */
    @Transactional
    public CheckoutSummaryDTO checkout(CheckoutRequest request, HttpSession session) {
        // Nhóm các item theo ID để tính quantity
        Map<Long, Long> itemCounts = request.getItemIds().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                    Objects::requireNonNull,
                    Collectors.counting()
                ));

        List<OrderService.OrderItemData> orderItems = new java.util.ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Long> entry : itemCounts.entrySet()) {
            Long itemId = entry.getKey();
            int quantity = entry.getValue().intValue();
            
            ClothingItem item = clothingRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id=" + itemId));
            
            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(quantity));
            total = total.add(itemTotal);
            
            orderItems.add(new OrderService.OrderItemData(itemId, quantity, item.getPrice()));
        }

        // Tạo mã đơn hàng
        String orderCode = "ORD-" + (100000 + random.nextInt(900000));

        // Lấy user ID từ session
        Long userId = (Long) session.getAttribute("userId");

        // Lưu đơn hàng vào database và tự động trừ kho
        orderService.createOrder(
                orderCode,
                userId, // có thể null nếu chưa đăng nhập
                total,
                request.getPaymentMethod(),
                PaymentStatus.PENDING,
                OrderStatus.PENDING,
                null, // shippingAddress - có thể lấy từ request hoặc user profile
                request.getPhone(),
                request.getCustomerName(),
                null, // notes
                orderItems
        );

        // Tạo danh sách items để hiển thị trong DTO
        List<com.example.electronic.dto.ClothingItemDTO> itemDTOs = itemCounts.keySet().stream()
                .map(itemId -> catalogService.getItem(itemId))
                .collect(Collectors.toList());

        // Tạo DTO để trả về
        return new CheckoutSummaryDTO(orderCode, total, PaymentStatus.PENDING,
                request.getPaymentMethod(), LocalDateTime.now(), itemDTOs);
    }

    /**
     * Xác nhận thanh toán ngân hàng với mã giao dịch
     */
    @Transactional
    public void confirmBankTransfer(String orderCode, String transactionCode) {
        // Tìm đơn hàng theo mã
        var orderOpt = orderService.getOrderByCode(orderCode);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + orderCode);
        }
        
        Order order = orderOpt.get();
        
        // Kiểm tra mã giao dịch
        if (transactionCode == null || transactionCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giao dịch không hợp lệ");
        }
        
        // Lưu mã giao dịch vào notes của đơn hàng
        String currentNotes = order.getNotes();
        String newNotes = (currentNotes != null && !currentNotes.isEmpty() ? currentNotes + "\n" : "") + 
                         "Mã giao dịch ngân hàng: " + transactionCode + " - Xác nhận: " + LocalDateTime.now();
        order.setNotes(newNotes);
        
        // Cập nhật trạng thái thanh toán thành COMPLETED (tự động cập nhật trong admin)
        order.setPaymentStatus(PaymentStatus.COMPLETED);
        orderService.saveOrder(order);
    }
}

