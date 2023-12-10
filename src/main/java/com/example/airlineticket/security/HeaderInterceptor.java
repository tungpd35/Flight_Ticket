package com.example.airlineticket.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HeaderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Thực hiện các hành động trước khi request được xử lý bởi controller
        String token = getCookie(request,"token");
        String accessToken = "your_access_token";
        request.setAttribute("Authorization", "Bearer " + accessToken);
        // Có thể thêm các header khác ở đây
        return true; // Cho phép request tiếp tục xử lý
    }
    public String getCookie(HttpServletRequest request,String name) {
        // Lấy danh sách các cookie từ request
        Cookie[] cookies = request.getCookies();

        // Kiểm tra nếu danh sách cookie không null và không rỗng
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    String cookieValue = cookie.getValue();
                    return  cookieValue;
                }
            }
        }
        return "Cookie not found";
    }
}
