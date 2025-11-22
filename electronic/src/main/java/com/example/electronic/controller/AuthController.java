package com.example.electronic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.electronic.model.UserAccount;
import com.example.electronic.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String registerForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam(required = false) String email,
                                 Model model,
                                 HttpSession session) {
        if (authService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại");
            return "auth/register";
        }
        UserAccount u = authService.register(username, password, email);
        session.setAttribute("userId", u.getId());
        session.setAttribute("username", u.getUsername());
        session.setAttribute("role", u.getRole());
        return "redirect:/shop/home";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
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
                    
                    // Redirect đến trang được yêu cầu hoặc trang chủ
                    if (redirect != null && !redirect.isEmpty()) {
                        return "redirect:" + redirect;
                    }
                    return "redirect:/shop/home";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
                    if (redirect != null) {
                        model.addAttribute("redirect", redirect);
                    }
                    return "auth/login";
                });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/shop/home";
    }
}
