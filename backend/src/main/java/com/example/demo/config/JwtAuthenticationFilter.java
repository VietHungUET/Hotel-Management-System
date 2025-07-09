package com.example.demo.config; // Đặt trong package config hoặc security

import com.example.demo.service.CustomDetailsService;
import com.example.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomDetailsService customDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Bỏ qua xác thực JWT cho endpoint đăng nhập và đăng ký
        if (request.getServletPath().contains("/login") || request.getServletPath().contains("/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Kiểm tra xem header Authorization có tồn tại và bắt đầu bằng "Bearer " không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Chuyển tiếp yêu cầu nếu không có JWT
            return;
        }

        // Trích xuất JWT từ header
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt); // Trích xuất tên người dùng từ JWT

        // Nếu tên người dùng không null và chưa có thông tin xác thực trong SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customDetailsService.loadUserByUsername(username);

            // Kiểm tra tính hợp lệ của token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Tạo đối tượng xác thực
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Mật khẩu không cần thiết sau khi xác thực token
                        userDetails.getAuthorities() // Lấy quyền hạn từ UserDetails
                );
                // Đặt chi tiết xác thực web
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Đặt đối tượng xác thực vào SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response); // Chuyển tiếp yêu cầu đến bộ lọc tiếp theo
    }
}