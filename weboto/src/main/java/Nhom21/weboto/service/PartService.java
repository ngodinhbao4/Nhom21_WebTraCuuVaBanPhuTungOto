package Nhom21.weboto.service;

import Nhom21.weboto.dto.PartDTO;
import Nhom21.weboto.entity.Category;
import Nhom21.weboto.entity.ModelYear;
import Nhom21.weboto.entity.Part;
import Nhom21.weboto.entity.PartCompatibility;
import Nhom21.weboto.repository.CategoryRepository;
import Nhom21.weboto.repository.ModelYearRepository;
import Nhom21.weboto.repository.PartCompatibilityRepository;
import Nhom21.weboto.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartService {

    @Autowired private PartRepository partRepository;
    @Autowired private ModelYearRepository modelYearRepository;
    @Autowired private PartCompatibilityRepository compatibilityRepository;
    @Autowired private CategoryRepository categoryRepository;

    // --- 1. Tra cứu thông minh (Dành cho User) ---
    public List<PartDTO> searchParts(Integer modelYearId, String keyword) {
        return partRepository.searchParts(modelYearId, keyword).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    // --- 2. Thêm mới Phụ tùng (Dành cho Admin) ---
    public PartDTO save(PartDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        Part part = new Part();
        mapDtoToEntity(dto, part);
        part.setCategory(category);

        Part savedPart = partRepository.save(part);
        return mapEntityToDto(savedPart);
    }

    // --- 3. Cập nhật Phụ tùng (Dành cho Admin) ---
    @Transactional
    public PartDTO update(Integer id, PartDTO dto) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng"));
        
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        mapDtoToEntity(dto, part);
        part.setCategory(category);

        return mapEntityToDto(partRepository.save(part));
    }

    // --- 4. Thiết lập tương thích (Compatibility) ---
    @Transactional
    public void addCompatibility(Integer partId, Integer modelYearId) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ tùng"));
        ModelYear modelYear = modelYearRepository.findById(modelYearId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đời xe"));

        PartCompatibility compatibility = new PartCompatibility();
        compatibility.setPart(part);
        compatibility.setModelYear(modelYear);
        
        compatibilityRepository.save(compatibility);
    }

    // Hàm Helper chuyển đổi dữ liệu
    private void mapDtoToEntity(PartDTO dto, Part part) {
        part.setName(dto.getName());
        part.setSku(dto.getSku());
        part.setDescription(dto.getDescription());
        part.setPrice(dto.getPrice());
        part.setStockQuantity(dto.getStockQuantity());
        part.setImageUrl(dto.getImageUrl());
    }

    private PartDTO mapEntityToDto(Part part) {
        return new PartDTO(
                part.getId(),
                part.getName(),
                part.getSku(),
                part.getDescription(),
                part.getPrice(),
                part.getStockQuantity(),
                part.getImageUrl(),
                part.getCategory() != null ? part.getCategory().getId() : null,
                part.getCategory() != null ? part.getCategory().getName() : "Chưa phân loại"
        );
    }

    public List<PartDTO> findAll() {
        return partRepository.findAll().stream()
                .map(this::mapEntityToDto) // Sử dụng hàm helper đã viết ở bước trước
                .collect(Collectors.toList());
    }
}