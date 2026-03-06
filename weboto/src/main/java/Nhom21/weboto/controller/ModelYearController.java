package Nhom21.weboto.controller;

import Nhom21.weboto.dto.ModelYearDTO;
import Nhom21.weboto.service.ModelYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/models")
public class ModelYearController {

    @Autowired
    private ModelYearService modelYearService;

    /**
     * API: GET /api/v1/model-years/car-model/{modelId}
     * Chức năng: Lấy danh sách năm sản xuất theo dòng xe [cite: 19]
     */
    @GetMapping("{modelId}/years")
    public ResponseEntity<List<ModelYearDTO>> getYearsByModel(@PathVariable Integer modelId) {
        List<ModelYearDTO> years = modelYearService.getYearsByModel(modelId);
        return ResponseEntity.ok(years);
    }
}