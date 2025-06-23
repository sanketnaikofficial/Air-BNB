package com.PropertyService.Service;

import com.PropertyService.Dto.APIResponse;
import com.PropertyService.Dto.CityDto;
import com.PropertyService.Entity.City;
import com.PropertyService.Repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityService {
	@Autowired
    private final CityRepository cityRepository;

    public APIResponse<CityDto> createCity(CityDto dto) {
        if (cityRepository.findByCityName(dto.getCityName()) != null) {
            return new APIResponse<>("Duplicate city", 409, null);
        }

        City city = new City();
        BeanUtils.copyProperties(dto, city);
        cityRepository.save(city);
        dto.setId(city.getId());
        

        return new APIResponse<>("City created", 201, dto);
    }

    public APIResponse<CityDto> getCityById(Long id) {
        Optional<City> city = cityRepository.findById(id);
        if (!city.isPresent()) {
            return new APIResponse<>("City not found", 404, null);
        }

        CityDto dto = new CityDto();
        BeanUtils.copyProperties(city.get(), dto);
        return new APIResponse<>("City fetched", 200, dto);
    }

    public APIResponse<List<CityDto>> getAllCities() {
        List<CityDto> dtos = cityRepository.findAll()
                .stream()
                .map(city -> {
                    CityDto dto = new CityDto();
                    BeanUtils.copyProperties(city, dto);
                    return dto;
                }).collect(Collectors.toList());
        return new APIResponse<>("Cities fetched", 200, dtos);
    }

    public APIResponse<String> deleteCity(Long id) {
        Optional<City> city = cityRepository.findById(id);
        if (!city.isPresent()) {
            return new APIResponse<>("City not found", 404, null);
        }
        cityRepository.delete(city.get());
        return new APIResponse<>("City deleted", 200, "Deleted");
    }
}
