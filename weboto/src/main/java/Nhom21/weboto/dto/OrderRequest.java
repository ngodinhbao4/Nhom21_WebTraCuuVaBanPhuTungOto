package Nhom21.weboto.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String address;
    private String paymentMethod;
}