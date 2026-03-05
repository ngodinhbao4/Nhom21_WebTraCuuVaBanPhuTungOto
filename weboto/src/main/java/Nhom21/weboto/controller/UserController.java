package Nhom21.weboto.controller;

import Nhom21.weboto.dto.PasswordRequest;
import Nhom21.weboto.dto.UserDTO;
import Nhom21.weboto.entity.User;
import Nhom21.weboto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Lấy thông tin cá nhân của người dùng hiện tại [cite: 34]
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet()));
        return ResponseEntity.ok(dto);
    }

    // Cập nhật email cá nhân [cite: 35]
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(Principal principal, @RequestBody UserDTO dto) {
        userService.updateEmail(principal.getName(), dto.getEmail());
        return ResponseEntity.ok("Cập nhật thông tin thành công!");
    }

    // Đổi mật khẩu cá nhân 
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(Principal principal, @RequestBody PasswordRequest request) {
        try {
            userService.changePassword(principal.getName(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Đổi mật khẩu thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
