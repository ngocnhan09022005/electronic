package com.example.electronic.controller;

import com.example.electronic.dto.CheckoutSummaryDTO;
import com.example.electronic.model.Order;
import com.example.electronic.model.PaymentMethod;
import com.example.electronic.request.CheckoutRequest;
import com.example.electronic.service.CartService;
import com.example.electronic.service.CatalogService;
import com.example.electronic.service.CheckoutService;
import com.example.electronic.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CatalogService catalogService;
    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CheckoutService checkoutService, CatalogService catalogService,
                             CartService cartService, OrderService orderService) {
        this.checkoutService = checkoutService;
        this.catalogService = catalogService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ConcurrentHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @ModelAttribute("paymentMethods")
    public PaymentMethod[] paymentMethods() {
        return PaymentMethod.values();
    }

    @GetMapping
    public String checkoutForm(Model model, HttpSession session) {
        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login?redirect=/checkout";
        }
        
        // Lấy sản phẩm từ giỏ hàng
        Map<Long, Integer> cart = getCart(session);
        var cartItems = cartService.getCartItems(cart);
        
        // Nếu giỏ hàng trống, hiển thị trang thông báo thay vì redirect
        if (cartItems.isEmpty()) {
            return "checkout/empty-cart";
        }
        
        CheckoutRequest request = new CheckoutRequest();
        model.addAttribute("checkoutRequest", request);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartService.getCartTotal(cart));
        model.addAttribute("bankAccountCode", "970422"); // Mã tài khoản ngân hàng - bạn có thể thay đổi
        return "checkout/payment";
    }

    @PostMapping
    public String submitCheckout(@Valid @ModelAttribute("checkoutRequest") CheckoutRequest checkoutRequest,
                                 BindingResult bindingResult,
                                 Model model,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("userId") == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để thực hiện thanh toán");
            return "redirect:/login";
        }
        
        // Lấy sản phẩm từ giỏ hàng
        Map<Long, Integer> cart = getCart(session);
        var cartItems = cartService.getCartItems(cart);
        
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống");
            return "redirect:/cart";
        }
        
        // Chuyển đổi cart items thành itemIds cho CheckoutRequest
        checkoutRequest.setItemIds(cartItems.stream()
                .flatMap(ci -> {
                    // Tạo danh sách itemId với số lượng
                    java.util.List<Long> ids = new java.util.ArrayList<>();
                    for (int i = 0; i < ci.getQuantity(); i++) {
                        ids.add(ci.getItem().getId());
                    }
                    return ids.stream();
                })
                .collect(java.util.stream.Collectors.toList()));
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("cartTotal", cartService.getCartTotal(cart));
            return "checkout/payment";
        }
        
        // Tạo đơn hàng
        CheckoutSummaryDTO summary = checkoutService.checkout(checkoutRequest, session);
        
        // Nếu thanh toán bằng chuyển khoản ngân hàng, redirect đến trang nhập mã
        if (checkoutRequest.getPaymentMethod() == PaymentMethod.BANK_TRANSFER) {
            // Xóa giỏ hàng sau khi tạo đơn hàng
            cartService.clearCart(cart);
            redirectAttributes.addFlashAttribute("orderCode", summary.getOrderCode());
            return "redirect:/checkout/bank-transfer?orderCode=" + summary.getOrderCode();
        }
        
        // Các phương thức thanh toán khác - thanh toán thành công ngay
        // Xóa giỏ hàng sau khi thanh toán thành công
        cartService.clearCart(cart);
        
        // Redirect đến trang success với orderCode
        redirectAttributes.addFlashAttribute("orderCode", summary.getOrderCode());
        return "redirect:/checkout/success?orderCode=" + summary.getOrderCode();
    }

    /**
     * Xác nhận thanh toán ngân hàng với mã giao dịch
     */
    @PostMapping("/confirm-bank-transfer")
    public String confirmBankTransfer(@RequestParam String transactionCode,
                                     @RequestParam String orderCode,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        try {
            checkoutService.confirmBankTransfer(orderCode, transactionCode);
            // Xóa giỏ hàng
            Map<Long, Integer> cart = getCart(session);
            cartService.clearCart(cart);
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công! Đơn hàng đã được xác nhận và tự động cập nhật trong admin.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/checkout/bank-transfer";
        }
        
        return "redirect:/checkout/success?orderCode=" + orderCode;
    }

    /**
     * Trang thanh toán ngân hàng (nếu cần truy cập trực tiếp)
     */
    @GetMapping("/bank-transfer")
    public String bankTransferForm(@RequestParam(required = false) String orderCode,
                                  Model model, HttpSession session) {
        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login?redirect=/checkout/bank-transfer";
        }
        
        if (orderCode != null) {
            // Lấy thông tin đơn hàng từ database
            var orderOpt = orderService.getOrderByCode(orderCode);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                CheckoutSummaryDTO summary = new CheckoutSummaryDTO(
                    order.getOrderCode(),
                    order.getTotalAmount(),
                    order.getPaymentStatus(),
                    order.getPaymentMethod(),
                    order.getCreatedAt(),
                    java.util.Collections.emptyList()
                );
                model.addAttribute("summary", summary);
                model.addAttribute("bankAccountCode", "970422");
                return "checkout/bank-transfer";
            }
        }
        return "redirect:/checkout";
    }

    /**
     * Trang thành công
     */
    @GetMapping("/success")
    public String successPage(@RequestParam(required = false) String orderCode,
                             Model model, HttpSession session) {
        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        
        if (orderCode != null) {
            var orderOpt = orderService.getOrderByCode(orderCode);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                // Lấy order items để hiển thị
                var orderItems = orderService.getOrderById(order.getId())
                    .map(Order::getItems)
                    .orElse(java.util.Collections.emptyList());
                
                List<com.example.electronic.dto.ClothingItemDTO> itemDTOs = orderItems.stream()
                    .map(item -> catalogService.getItem(item.getClothingItemId()))
                    .collect(java.util.stream.Collectors.toList());
                
                CheckoutSummaryDTO summary = new CheckoutSummaryDTO(
                    order.getOrderCode(),
                    order.getTotalAmount(),
                    order.getPaymentStatus(),
                    order.getPaymentMethod(),
                    order.getCreatedAt(),
                    itemDTOs
                );
                model.addAttribute("summary", summary);
            }
        }
        return "checkout/success";
    }
}

