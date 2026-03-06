package Nhom21.weboto.service;

import Nhom21.weboto.dto.ModelYearDTO;
import Nhom21.weboto.repository.ModelYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelYearService {

    @Autowired
    private ModelYearRepository modelYearRepository;

    /**
     * Lấy danh sách đời xe (năm) dựa theo ID của dòng xe [cite: 7]
     */
    public List<ModelYearDTO> getYearsByModel(Integer modelId) {
    return modelYearRepository.findByCarModelId(modelId).stream()
            .map(year -> new ModelYearDTO(year.getId(), year.getYearNumber())) // Dùng getter mới
            .collect(Collectors.toList());
    }
}