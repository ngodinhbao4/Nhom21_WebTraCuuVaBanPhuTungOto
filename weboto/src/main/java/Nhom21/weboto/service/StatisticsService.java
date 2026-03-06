package Nhom21.weboto.service;

import Nhom21.weboto.dto.RevenueDTO;
import Nhom21.weboto.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private OrderRepository orderRepository;

    public List<RevenueDTO> getDailyStatistics() {
        List<Object[]> results = orderRepository.getDailyRevenueNative();
        return results.stream()
                .map(result -> new RevenueDTO((String) result[0], (Double) result[1]))
                .collect(Collectors.toList());
    }

    public List<RevenueDTO> getMonthlyStatistics() {
        List<Object[]> results = orderRepository.getMonthlyRevenueNative();
        return results.stream()
                .map(result -> new RevenueDTO((String) result[0], (Double) result[1]))
                .collect(Collectors.toList());
    }
}
