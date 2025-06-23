package com.PropertyService.Controller;

import com.PropertyService.Dto.APIResponse;
import com.PropertyService.Dto.CityDto;
import com.PropertyService.Service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<CityDto>> createCity(@RequestBody CityDto dto) {
        return ResponseEntity.ok(cityService.createCity(dto));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<APIResponse<CityDto>> getCity(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<APIResponse<List<CityDto>>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<APIResponse<String>> deleteCity(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.deleteCity(id));
    }
}
