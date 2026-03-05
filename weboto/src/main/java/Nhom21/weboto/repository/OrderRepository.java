package Nhom21.weboto.repository;

import Nhom21.weboto.entity.Order; // Giả định thực thể Order đã tồn tại
import Nhom21.weboto.dto.RevenueDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Thống kê doanh thu theo từng ngày trong tháng hiện tại
    @Query("SELECT new Nhom21.weboto.dto.RevenueDTO(CAST(o.createdAt AS string), SUM(o.totalAmount)) " +
           "FROM Order o WHERE o.status = 'COMPLETED' " +
           "GROUP BY o.createdAt ORDER BY o.createdAt ASC")
    List<RevenueDTO> getDailyRevenue();

    // Thống kê doanh thu theo tháng trong năm hiện tại
    @Query("SELECT new Nhom21.weboto.dto.RevenueDTO(SUBSTRING(CAST(o.createdAt AS string), 1, 7), SUM(o.totalAmount)) " +
           "FROM Order o WHERE o.status = 'COMPLETED' " +
           "GROUP BY SUBSTRING(CAST(o.createdAt AS string), 1, 7)")
    List<RevenueDTO> getMonthlyRevenue();
}
