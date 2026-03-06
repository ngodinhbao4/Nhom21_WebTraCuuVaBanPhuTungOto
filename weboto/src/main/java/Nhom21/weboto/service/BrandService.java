package Nhom21.weboto.service;

import Nhom21.weboto.dto.BrandDTO;
import Nhom21.weboto.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    /**
     * Lấy danh sách toàn bộ hãng xe để hiển thị ở trang chủ hoặc bộ lọc [cite: 5, 19]
     */
    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brand -> new BrandDTO(brand.getId(), brand.getName(), brand.getLogoUrl()))
                .collect(Collectors.toList());
    }
}