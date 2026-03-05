package Nhom21.weboto.controller;

import Nhom21.weboto.dto.AdminUserDTO;
import Nhom21.weboto.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    // API lấy danh sách toàn bộ người dùng 
    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    // API Khóa hoặc mở khóa tài khoản [cite: 65]
    // Ví dụ: PUT /api/v1/admin/users/1/status?status=false (để khóa)
    @PutMapping("/{userId}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable Integer userId, 
            @RequestParam Boolean status) {
        try {
            adminUserService.toggleUserStatus(userId, status);
            String message = status ? "Đã mở khóa tài khoản!" : "Đã khóa tài khoản thành công!";
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
