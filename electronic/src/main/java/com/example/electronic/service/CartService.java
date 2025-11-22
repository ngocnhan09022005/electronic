package com.example.electronic.service;

import com.example.electronic.dto.ClothingItemDTO;
import com.example.electronic.repository.ClothingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final ClothingRepository clothingRepository;
    private final CatalogService catalogService;

    public CartService(ClothingRepository clothingRepository, CatalogService catalogService) {
        this.clothingRepository = clothingRepository;
        this.catalogService = catalogService;
    }

    /**
     * Thêm sản phẩm vào giỏ hàng (lưu trong session)
     */
    public void addToCart(Map<Long, Integer> cart, Long itemId, int quantity) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        
        // Kiểm tra sản phẩm có tồn tại không
        clothingRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm id=" + itemId));
        
        // Thêm vào giỏ hàng
        cart.put(itemId, cart.getOrDefault(itemId, 0) + quantity);
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ hàng
     */
    public void updateCartItem(Map<Long, Integer> cart, Long itemId, int quantity) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        if (quantity <= 0) {
            removeFromCart(cart, itemId);
            return;
        }
        cart.put(itemId, quantity);
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    public void removeFromCart(Map<Long, Integer> cart, Long itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        cart.remove(itemId);
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void clearCart(Map<Long, Integer> cart) {
        cart.clear();
    }

    /**
     * Lấy danh sách sản phẩm trong giỏ hàng với thông tin đầy đủ
     */
    public List<CartItemDTO> getCartItems(Map<Long, Integer> cart) {
        List<CartItemDTO> items = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            try {
                ClothingItemDTO item = catalogService.getItem(entry.getKey());
                items.add(new CartItemDTO(item, entry.getValue()));
            } catch (Exception e) {
                // Nếu sản phẩm không tồn tại, bỏ qua
            }
        }
        return items;
    }

    /**
     * Tính tổng tiền giỏ hàng
     */
    public BigDecimal getCartTotal(Map<Long, Integer> cart) {
        return getCartItems(cart).stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Lấy tổng số lượng sản phẩm trong giỏ hàng
     */
    public int getCartItemCount(Map<Long, Integer> cart) {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * DTO cho cart item
     */
    public static class CartItemDTO {
        private ClothingItemDTO item;
        private int quantity;

        public CartItemDTO(ClothingItemDTO item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public ClothingItemDTO getItem() {
            return item;
        }

        public void setItem(ClothingItemDTO item) {
            this.item = item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getSubtotal() {
            return item.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }
}

