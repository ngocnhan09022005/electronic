package com.example.electronic.controller;

import com.example.electronic.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Lấy giỏ hàng từ session hoặc tạo mới
     */
    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ConcurrentHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    /**
     * Xem giỏ hàng
     */
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        model.addAttribute("cartItems", cartService.getCartItems(cart));
        model.addAttribute("cartTotal", cartService.getCartTotal(cart));
        model.addAttribute("itemCount", cartService.getCartItemCount(cart));
        return "cart/view";
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @PostMapping("/add")
    public String addToCart(@RequestParam Long itemId,
                           @RequestParam(defaultValue = "1") int quantity,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        try {
            Map<Long, Integer> cart = getCart(session);
            cartService.addToCart(cart, itemId, quantity);
            redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        
        String referer = (String) session.getAttribute("lastViewedPage");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/shop/home";
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ hàng
     */
    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long itemId,
                                @RequestParam int quantity,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            Map<Long, Integer> cart = getCart(session);
            cartService.updateCartItem(cart, itemId, quantity);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long itemId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            Map<Long, Integer> cart = getCart(session);
            cartService.removeFromCart(cart, itemId);
            redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        Map<Long, Integer> cart = getCart(session);
        cartService.clearCart(cart);
        redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ giỏ hàng!");
        return "redirect:/cart";
    }

    /**
     * API để lấy số lượng sản phẩm trong giỏ hàng (cho AJAX)
     */
    @GetMapping("/count")
    @ResponseBody
    public int getCartCount(HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        return cartService.getCartItemCount(cart);
    }
}

