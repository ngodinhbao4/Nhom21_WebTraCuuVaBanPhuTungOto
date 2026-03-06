package Nhom21.weboto.service;

import Nhom21.weboto.dto.ModelYearDTO;
import Nhom21.weboto.entity.CarModel;
import Nhom21.weboto.entity.ModelYear;
import Nhom21.weboto.repository.CarModelRepository;
import Nhom21.weboto.repository.ModelYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            .map(year -> new ModelYearDTO(year.getId(), year.getYearNumber(), year.getCarModel().getId())) // Dùng getter mới
            .collect(Collectors.toList());
    }

    @Autowired
    private CarModelRepository carModelRepository; // Cần inject để tìm Dòng xe cha

    public ModelYearDTO save(ModelYearDTO dto) {
        // 1. Tìm Dòng xe dựa trên modelId
        CarModel carModel = carModelRepository.findById(dto.getModelId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Dòng xe ID: " + dto.getModelId()));
        
        // 2. Chuyển DTO sang Entity
        ModelYear modelYear = new ModelYear();
        modelYear.setYearNumber(dto.getYearNumber());
        modelYear.setCarModel(carModel);
        
        // 3. Lưu và trả về DTO
        ModelYear savedYear = modelYearRepository.save(modelYear);
        return new ModelYearDTO(savedYear.getId(), savedYear.getYearNumber(), savedYear.getCarModel().getId());
    }

    public List<ModelYearDTO> findAll() {
        return modelYearRepository.findAll().stream()
                .map(year -> new ModelYearDTO(year.getId(), year.getYearNumber(), year.getCarModel().getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ModelYearDTO update(Integer id, ModelYearDTO dto) {
        ModelYear year = modelYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đời xe"));
        CarModel model = carModelRepository.findById(dto.getModelId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dòng xe mới"));

        year.setYearNumber(dto.getYearNumber());
        year.setCarModel(model);
        ModelYear saved = modelYearRepository.save(year);
        return new ModelYearDTO(saved.getId(), saved.getYearNumber(), saved.getCarModel().getId());
    }
}