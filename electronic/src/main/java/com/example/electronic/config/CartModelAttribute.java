package com.example.electronic.config;

import com.example.electronic.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@Component
@ControllerAdvice
public class CartModelAttribute {

    private final CartService cartService;

    public CartModelAttribute(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cartCount")
    public Integer getCartCount(HttpSession session) {
        if (session == null) {
            return 0;
        }
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            return 0;
        }
        return cartService.getCartItemCount(cart);
    }
}

