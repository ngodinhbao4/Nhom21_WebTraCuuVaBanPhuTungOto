package Nhom21.weboto.repository;

import Nhom21.weboto.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Integer> {
    // Tìm kiếm phụ tùng theo tên (Discovery API) 
    List<Part> findByNameContainingIgnoreCase(String name);
    
    // Lấy phụ tùng theo danh mục
    List<Part> findByCategoryId(Integer categoryId);

    @Query("SELECT DISTINCT p FROM Part p " +
           "LEFT JOIN PartCompatibility pc ON p.id = pc.part.id " +
           "WHERE (:modelYearId IS NULL OR pc.modelYear.id = :modelYearId) " +
           "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Part> searchParts(@Param("modelYearId") Integer modelYearId, 
                           @Param("keyword") String keyword);
}