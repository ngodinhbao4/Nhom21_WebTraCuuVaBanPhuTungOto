package Nhom21.weboto.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Integer partId;
    private Integer quantity;
}