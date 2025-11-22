package com.example.electronic.config;

import com.example.electronic.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminAuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        
        // Cho phép truy cập trang login admin
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/admin/login")) {
            return true;
        }
        
        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("/admin/login?redirect=" + requestURI);
            return false;
        }
        
        // Kiểm tra quyền admin (optional - có thể bỏ qua nếu cho phép tất cả user đã đăng nhập)
        Long userId = (Long) session.getAttribute("userId");
        return authService.findById(userId)
                .map(user -> {
                    // Nếu muốn chỉ admin mới truy cập được, bỏ comment dòng dưới:
                    // return user.isAdmin();
                    // Hiện tại cho phép tất cả user đã đăng nhập truy cập admin
                    return true;
                })
                .orElseGet(() -> {
                    try {
                        response.sendRedirect("/admin/login?error=not_found");
                    } catch (Exception e) {
                        // Ignore
                    }
                    return false;
                });
    }
}

