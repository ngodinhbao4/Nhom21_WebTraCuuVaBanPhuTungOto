package Nhom21.weboto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF cho REST API
            .authorizeHttpRequests(auth -> auth
                // 1. API công khai (Đăng ký, Đăng nhập, Tra cứu) [cite: 29-33, 38-43]
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/brands/**", "/api/v1/parts/**", "/api/v1/categories/**").permitAll()
                
                // 2. API dành riêng cho Admin [cite: 14, 53-66]
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                
                // 3. API dành cho User đã đăng nhập [cite: 22, 44-52]
                .requestMatchers("/api/v1/users/**", "/api/v1/cart/**", "/api/v1/orders/**").hasAnyRole("USER", "ADMIN")
                
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults()); // Sử dụng Basic Auth cho giai đoạn đầu (có thể thay bằng JWT sau)
        
        return http.build();
    }
}