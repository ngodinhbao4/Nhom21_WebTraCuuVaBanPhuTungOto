package Nhom21.weboto.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Integer> {
    
    // Tìm kiếm mã OTP mới nhất của một User để xác thực
    Optional<OtpVerification> findByOtpCodeAndUser(String otpCode, User user);
    
    // Xóa các mã OTP cũ sau khi đã xác thực thành công
    void deleteByUser(User user);
}
