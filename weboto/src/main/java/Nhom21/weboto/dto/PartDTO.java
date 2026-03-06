package Nhom21.weboto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartDTO {
    private Integer id;
    private String name;
    private String sku;
    private Double price;
    private String imageUrl;
    private String categoryName;
}