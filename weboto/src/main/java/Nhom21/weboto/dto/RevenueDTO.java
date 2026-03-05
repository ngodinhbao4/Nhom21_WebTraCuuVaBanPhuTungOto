package Nhom21.weboto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueDTO {
    private String period; // Ví dụ: "2026-03-05" hoặc "Tháng 03"
    private Double totalRevenue;
}
