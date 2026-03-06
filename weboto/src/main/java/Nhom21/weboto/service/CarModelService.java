package Nhom21.weboto.service;

import Nhom21.weboto.dto.CarModelDTO;
import Nhom21.weboto.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarModelService {

    @Autowired
    private CarModelRepository carModelRepository;

    /**
     * Lấy danh sách dòng xe dựa theo ID của hãng
     */
    public List<CarModelDTO> getModelsByBrand(Integer brandId) {
        return carModelRepository.findByBrandId(brandId).stream()
                .map(model -> new CarModelDTO(model.getId(), model.getModelName())) // Dùng getter mới
                .collect(Collectors.toList());
    }
}