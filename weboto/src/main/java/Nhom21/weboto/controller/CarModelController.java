package Nhom21.weboto.controller;

import Nhom21.weboto.dto.CarModelDTO;
import Nhom21.weboto.service.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
public class CarModelController {

    @Autowired
    private CarModelService carModelService;

    /**
     * API: GET /api/v1/car-models/brand/{brandId}
     * Chức năng: Lấy danh sách dòng xe theo hãng xe [cite: 6, 19]
     */
    @GetMapping("/{brandId}/models")
    public ResponseEntity<List<CarModelDTO>> getModelsByBrand(@PathVariable Integer brandId) {
        List<CarModelDTO> models = carModelService.getModelsByBrand(brandId);
        return ResponseEntity.ok(models);
    }
}