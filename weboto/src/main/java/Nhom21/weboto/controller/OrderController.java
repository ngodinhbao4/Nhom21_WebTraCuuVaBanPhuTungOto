package Nhom21.weboto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Nhom21.weboto.dto.OrderDTO;
import Nhom21.weboto.dto.OrderRequest;
import Nhom21.weboto.entity.User;
import Nhom21.weboto.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired private OrderService orderService;

    // POST /api/v1/orders: Thanh toán
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody OrderRequest req) {
        return ResponseEntity.ok(orderService.checkout(user, req.getAddress(), req.getPaymentMethod()));
    }

    // GET /api/v1/orders: Lịch sử đơn hàng
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getMyOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.findByUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer id) {
        try {
            OrderDTO orderDTO = orderService.getOrderById(id);
            return ResponseEntity.ok(orderDTO);
        } catch (RuntimeException ex) {
            // Nếu không tìm thấy ID đơn hàng, trả về thông báo lỗi thay vì 404 trống
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
