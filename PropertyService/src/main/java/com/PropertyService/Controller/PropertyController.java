package com.PropertyService.Controller;

import com.PropertyService.Dto.*;    
import com.PropertyService.Service.PropertyService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;



@Data
@RequiredArgsConstructor
@RequestMapping("/api/v1/property")
@RestController
public class PropertyController {
	
	@Autowired
    private final PropertyService propertyService;

  

	@PostMapping("/create-property-with-images")
	public ResponseEntity<APIResponse<PropertyServiceDto>> createProperty(
	        @RequestPart("data") String data,
	        @RequestPart(value = "images", required = false) List<MultipartFile> images) {
	    try {
	        ObjectMapper mapper = new ObjectMapper();
	        PropertyServiceDto dto = mapper.readValue(data, PropertyServiceDto.class);
	        return ResponseEntity.ok(propertyService.createProperty(dto, images));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(new APIResponse<>("Invalid data", 400, null), HttpStatus.BAD_REQUEST);
	    }
	}

    // Helper method to check file extensions
    private boolean isImageFile(String filename) {
        if (filename == null) return false;
        String lowerCase = filename.toLowerCase();
        return lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".png");
    }
    @GetMapping("/search-property")
	public APIResponse searchProperty(
	        @RequestParam String name,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
	    APIResponse response = propertyService.searchProperty(name, date);
	    return response;
	}
    @GetMapping("/get-property-id")
    public ResponseEntity<APIResponse<PropertyServiceDto>> getPropertyById(@RequestParam Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse<String>> deleteProperty(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.deleteProperty(id));
    }
    

    }
