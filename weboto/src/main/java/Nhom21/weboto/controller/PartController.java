package Nhom21.weboto.controller;

import Nhom21.weboto.dto.PartDTO;
import Nhom21.weboto.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/parts")
public class PartController {

    @Autowired
    private PartService partService;

    // API: GET /api/v1/parts/search?modelYearId=10&keyword=má phanh
    @GetMapping("/search")
    public ResponseEntity<List<PartDTO>> searchParts(
            @RequestParam(required = false) Integer modelYearId,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(partService.searchParts(modelYearId, keyword));
    }
}