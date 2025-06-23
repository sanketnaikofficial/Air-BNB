package com.PropertyService.Service;

import com.PropertyService.Dto.APIResponse;
import com.PropertyService.Dto.AreaDto;
import com.PropertyService.Entity.Area;
import com.PropertyService.Repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaService {
	@Autowired
    private final AreaRepository areaRepository;

    public APIResponse<AreaDto> createArea(AreaDto dto) {
        if (areaRepository.findByAreaName(dto.getAreaName()) != null) {
            return new APIResponse<>("Duplicate area", 409, null);
        }

        Area area = new Area();
        BeanUtils.copyProperties(dto, area);
        areaRepository.save(area);
        dto.setId(area.getId());

        return new APIResponse<>("Area created", 201, dto);
    }

    public APIResponse<AreaDto> getAreaById(Long id) {
        Optional<Area> optional = areaRepository.findById(id);
        if (!optional.isPresent()) {
            return new APIResponse<>("Area not found", 404, null);
        }

        AreaDto dto = new AreaDto();
        BeanUtils.copyProperties(optional.get(), dto);
        return new APIResponse<>("Area fetched", 200, dto);
    }

    public APIResponse<List<AreaDto>> getAllAreas() {
        List<AreaDto> areas = areaRepository.findAll()
                .stream()
                .map(area -> {
                    AreaDto dto = new AreaDto();
                    BeanUtils.copyProperties(area, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return new APIResponse<>("Areas fetched", 200, areas);
    }

    public APIResponse<String> deleteArea(Long id) {
        Optional<Area> area = areaRepository.findById(id);
        if (!area.isPresent()) {
            return new APIResponse<>("Area not found", 404, null);
        }

        areaRepository.delete(area.get());
        return new APIResponse<>("Area deleted successfully", 200, "Deleted");
    }
}
