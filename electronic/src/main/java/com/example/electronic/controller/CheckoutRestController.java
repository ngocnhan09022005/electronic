package com.example.electronic.controller;

import com.example.electronic.dto.CheckoutSummaryDTO;
import com.example.electronic.request.CheckoutRequest;
import com.example.electronic.service.CheckoutService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutRestController {

    private final CheckoutService checkoutService;

    public CheckoutRestController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public CheckoutSummaryDTO checkout(@Valid @RequestBody CheckoutRequest request, HttpSession session) {
        return checkoutService.checkout(request, session);
    }
}

