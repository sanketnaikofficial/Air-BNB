package com.PropertyService.Controller;

import com.PropertyService.Dto.APIResponse;
import com.PropertyService.Dto.AreaDto;
import com.PropertyService.Service.AreaService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property/area")
@RequiredArgsConstructor
public class AreaController {
	@Autowired
    private final AreaService areaService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<AreaDto>> createArea(@RequestBody AreaDto dto) {
        return ResponseEntity.ok(areaService.createArea(dto));
    }

    @GetMapping("/get-area/{id}")
    public ResponseEntity<APIResponse<AreaDto>> getArea(@PathVariable Long id) {
        return ResponseEntity.ok(areaService.getAreaById(id));
    }

    @GetMapping("/get-all-areas")
    public ResponseEntity<APIResponse<List<AreaDto>>> getAllAreas() {
        return ResponseEntity.ok(areaService.getAllAreas());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse<String>> deleteArea(@PathVariable Long id) {
        return ResponseEntity.ok(areaService.deleteArea(id));
    }
}
