package Nhom21.weboto.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Nhom21.weboto.dto.OrderDTO;
import Nhom21.weboto.dto.OrderItemDTO;
import Nhom21.weboto.entity.CartItem;
import Nhom21.weboto.entity.Order;
import Nhom21.weboto.entity.OrderItem;
import Nhom21.weboto.entity.OrderStatus;
import Nhom21.weboto.entity.Part;
import Nhom21.weboto.entity.User;
import Nhom21.weboto.repository.CartItemRepository;
import Nhom21.weboto.repository.OrderRepository;
import Nhom21.weboto.repository.PartRepository;
import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private PartRepository partRepository;

    @Transactional
    public OrderDTO checkout(User user, String address, String paymentMethod) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) throw new RuntimeException("Giỏ hàng đang trống!");

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            Part part = ci.getPart();
            
            // 1. Kiểm tra tồn kho (stock_quantity trong SQL)
            if (part.getStockQuantity() < ci.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + part.getName() + " không đủ hàng!");
            }

            // 2. Trừ tồn kho
            part.setStockQuantity(part.getStockQuantity() - ci.getQuantity());
            partRepository.save(part);

            // 3. Tạo chi tiết đơn hàng
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setPart(part);
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(part.getPrice()); // Lưu unit_price từ bảng parts
            orderItems.add(oi);

            // 4. Cộng dồn tổng tiền
            total = total.add(part.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        order.setTotalPrice(total);
        order.setItems(orderItems);
        
        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems); // Xóa giỏ sau khi mua

        return mapToDTO(savedOrder);
    }
    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setCreatedAt(order.getCreatedAt());
        
        // Chuyển đổi danh sách món đồ trong đơn hàng
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(this::mapToItemDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private OrderItemDTO mapToItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setPartId(item.getPart().getId());
        dto.setPartName(item.getPart().getName());
        dto.setImageUrl(item.getPart().getImageUrl());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        return dto;
    }

    public List<OrderDTO> findByUser(User user) {
    // Gọi Repository để tìm danh sách đơn hàng theo User
        return orderRepository.findByUser(user).stream()
                .map(this::mapToDTO) // Sử dụng hàm mapping bạn đã có
                .collect(Collectors.toList());
    }

    // Lấy chi tiết một đơn hàng theo ID
    @Transactional
    public OrderDTO getOrderById(Integer id) {
        // 1. Tìm đơn hàng trong DB, nếu không thấy sẽ ném lỗi 404 thực sự
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID: " + id));

        // 2. Map từ Entity sang DTO để trả về cho Frontend/Postman
        return mapToDTO(order);
    }
}
