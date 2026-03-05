package Nhom21.weboto.controller;

import Nhom21.weboto.dto.RevenueDTO;
import Nhom21.weboto.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    // Lấy doanh thu theo ngày
    @GetMapping("/revenue/daily")
    public ResponseEntity<List<RevenueDTO>> getDailyRevenue() {
        return ResponseEntity.ok(statisticsService.getDailyStatistics());
    }

    // Lấy doanh thu theo tháng
    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<RevenueDTO>> getMonthlyRevenue() {
        return ResponseEntity.ok(statisticsService.getMonthlyStatistics());
    }
}
