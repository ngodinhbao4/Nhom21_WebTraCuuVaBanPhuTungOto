package Nhom21.weboto.security;

import Nhom21.weboto.entity.User;
import Nhom21.weboto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));

        // Kiểm tra tài khoản đã kích hoạt chưa 
        if (!user.getIsActive()) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt qua OTP");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName())) // Ánh xạ quyền từ DB [cite: 92, 105]
                    .collect(Collectors.toList())
        );
    }
}