package Nhom21.weboto.service;

import Nhom21.weboto.dto.CarModelDTO;
import Nhom21.weboto.entity.Brand;
import Nhom21.weboto.entity.CarModel;
import Nhom21.weboto.repository.BrandRepository;
import Nhom21.weboto.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarModelService {

    @Autowired
    private CarModelRepository carModelRepository;
    @Autowired
    private BrandRepository brandRepository; // Cần inject để tìm Hãng xe cha

    /**
     * Lấy danh sách dòng xe dựa theo ID của hãng
     */
    public List<CarModelDTO> getModelsByBrand(Integer brandId) {
        return carModelRepository.findByBrandId(brandId).stream()
                .map(model -> new CarModelDTO(model.getId(), model.getModelName(), model.getBrand().getId())) // Dùng getter mới
                .collect(Collectors.toList());
    }


    public CarModelDTO save(CarModelDTO dto) {
        // 1. Tìm Hãng xe dựa trên brandId
        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Hãng xe ID: " + dto.getBrandId()));
        
        // 2. Chuyển DTO sang Entity
        CarModel model = new CarModel();
        model.setModelName(dto.getName());
        model.setBrand(brand);
        
        // 3. Lưu và trả về DTO
        CarModel savedModel = carModelRepository.save(model);
        return new CarModelDTO(savedModel.getId(), savedModel.getModelName(), savedModel.getBrand().getId());
    }

    public List<CarModelDTO> findAll() {
    return carModelRepository.findAll().stream()
            .map(model -> new CarModelDTO(model.getId(), model.getModelName(), model.getBrand().getId()))
            .collect(Collectors.toList());
}

    @Transactional
    public CarModelDTO update(Integer id, CarModelDTO dto) {
        CarModel model = carModelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dòng xe"));
        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hãng xe mới"));
        
        model.setModelName(dto.getName());
        model.setBrand(brand);
        CarModel saved = carModelRepository.save(model);
        return new CarModelDTO(saved.getId(), saved.getModelName(), saved.getBrand().getId());
    }
}