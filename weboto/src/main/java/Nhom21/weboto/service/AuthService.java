package Nhom21.weboto.service;

import Nhom21.weboto.entity.*;
import Nhom21.weboto.repository.*;
import Nhom21.weboto.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpVerificationRepository otpRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Transactional
    public void register(String username, String rawPassword, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setIsActive(false);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền USER."));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        String otpCode = String.format("%06d", new Random().nextInt(999999));
        OtpVerification otp = new OtpVerification();
        otp.setOtpCode(otpCode);
        otp.setUser(user);
        otp.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        emailService.sendOtpEmail(email, otpCode);
    }

    @Transactional
    public void verifyOtp(String username, String code) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        OtpVerification otp = otpRepository.findByOtpCodeAndUser(code, user)
                .orElseThrow(() -> new RuntimeException("Mã OTP không chính xác"));

        if (otp.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã OTP đã hết hạn.");
        }

        user.setIsActive(true);
        userRepository.save(user);
        otpRepository.delete(otp);
    }

    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Sai tên đăng nhập hoặc mật khẩu"));

        if (!user.getIsActive()) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt qua OTP.");
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            return tokenProvider.generateToken(username);
        } else {
            throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu");
        }
    }
}