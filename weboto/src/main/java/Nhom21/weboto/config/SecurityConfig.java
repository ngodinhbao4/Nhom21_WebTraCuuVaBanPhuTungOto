package Nhom21.weboto.config;

import Nhom21.weboto.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Vô hiệu hóa CSRF vì chúng ta dùng Stateless JWT
            .csrf(csrf -> csrf.disable()) 
            
            // 2. Thiết lập chế độ Stateless (không lưu Session trên server)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 3. Cấu hình phân quyền API
            .authorizeHttpRequests(auth -> auth
                // API công khai: Đăng ký, Đăng nhập, Tra cứu phụ tùng
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/brands/**", "/api/v1/parts/**", "/api/v1/categories/**").permitAll()
                .requestMatchers("/api/v1/models/**").permitAll()

                // API dành riêng cho Admin (Thống kê, Quản lý User)
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                
                // API dành cho User & Admin (Profile, Giỏ hàng, Đơn hàng)
                .requestMatchers("/api/v1/users/**", "/api/v1/cart/**", "/api/v1/orders/**").hasAnyRole("USER", "ADMIN")
                
                // Các yêu cầu còn lại phải đăng nhập
                .anyRequest().authenticated()
            );

        // 4. THÊM QUAN TRỌNG: Chèn Filter kiểm tra JWT trước khi xử lý đăng nhập
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}