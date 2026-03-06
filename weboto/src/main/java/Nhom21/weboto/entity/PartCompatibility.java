package Nhom21.weboto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "part_compatibility")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PartCompatibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part; // Linh kiện nào?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_year_id", nullable = false)
    private ModelYear modelYear; // Lắp cho đời xe nào?

    @Column(length = 255)
    private String notes; // Ghi chú thêm (ví dụ: "Chỉ dành cho bản Turbo")
}