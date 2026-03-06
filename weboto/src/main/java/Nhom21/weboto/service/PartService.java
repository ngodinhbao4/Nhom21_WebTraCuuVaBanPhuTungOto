package Nhom21.weboto.service;

import Nhom21.weboto.dto.PartDTO;
import Nhom21.weboto.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;

    public List<PartDTO> searchParts(Integer modelYearId, String keyword) {
        return partRepository.searchParts(modelYearId, keyword).stream()
                .map(part -> new PartDTO(
                        part.getId(),
                        part.getName(),
                        part.getSku(),
                        part.getPrice(),
                        part.getImageUrl(),
                        part.getCategory() != null ? part.getCategory().getName() : "Chưa phân loại"
                )).collect(Collectors.toList());
    }
}