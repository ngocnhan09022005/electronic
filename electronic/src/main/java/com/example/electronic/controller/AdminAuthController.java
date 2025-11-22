package com.example.electronic.controller;

import com.example.electronic.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final AuthService authService;

    public AdminAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String redirect,
                           @RequestParam(required = false) String error,
                           Model model) {
        if (redirect != null) {
            model.addAttribute("redirect", redirect);
        }
        if (error != null) {
            model.addAttribute("error", "Bạn cần đăng nhập để truy cập khu vực quản trị");
        }
        return "admin/login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam(required = false) String redirect,
                             Model model,
                             HttpSession session) {
        return authService.authenticate(username, password)
                .map(u -> {
                    session.setAttribute("userId", u.getId());
                    session.setAttribute("username", u.getUsername());
                    session.setAttribute("role", u.getRole());
                    
                    // Redirect đến trang admin được yêu cầu hoặc dashboard
                    if (redirect != null && !redirect.isEmpty()) {
                        return "redirect:" + redirect;
                    }
                    return "redirect:/admin";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
                    if (redirect != null) {
                        model.addAttribute("redirect", redirect);
                    }
                    return "admin/login";
                });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}

