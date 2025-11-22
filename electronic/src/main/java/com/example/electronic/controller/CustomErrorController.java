package com.example.electronic.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("status", "404");
                model.addAttribute("error", "Không tìm thấy trang");
                model.addAttribute("message", message != null ? message : "Trang bạn đang tìm kiếm không tồn tại.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("status", "500");
                model.addAttribute("error", "Lỗi máy chủ");
                model.addAttribute("message", message != null ? message : "Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
            } else {
                model.addAttribute("status", statusCode);
                model.addAttribute("error", "Lỗi");
                model.addAttribute("message", message != null ? message : "Đã xảy ra lỗi không mong muốn.");
            }
        } else {
            model.addAttribute("status", "500");
            model.addAttribute("error", "Lỗi");
            model.addAttribute("message", message != null ? message : "Đã xảy ra lỗi không mong muốn.");
        }
        
        return "error";
    }
    
    public String getErrorPath() {
        return "/error";
    }
}

