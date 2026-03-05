package Nhom21.weboto.service;

import Nhom21.weboto.dto.RevenueDTO;
import Nhom21.weboto.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private OrderRepository orderRepository;

    public List<RevenueDTO> getDailyStatistics() {
        return orderRepository.getDailyRevenue();
    }

    public List<RevenueDTO> getMonthlyStatistics() {
        return orderRepository.getMonthlyRevenue();
    }
}
