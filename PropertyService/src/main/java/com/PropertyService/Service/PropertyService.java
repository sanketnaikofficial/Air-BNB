package com.PropertyService.Service;

import com.PropertyService.Constants.AppConstants;    
import com.PropertyService.Dto.*;
import com.PropertyService.Entity.*;
import com.PropertyService.Repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class PropertyService {
	@Autowired
    private final PropertyRepository propertyRepository;
	@Autowired
    private final PropertyImagesRepository propertyImagesRepository;
	@Autowired
    private final S3Service s3Service;
	@Autowired
    private final ImageProcessingService imageProcessingService;
	@Autowired
	private final CityRepository cityRepository;
	@Autowired
	private final AreaRepository areaRepository;
	@Autowired
	private final StateRepository stateRepository;
	@Autowired
	private final RoomsRepository roomsRepository;


	@Transactional
	public APIResponse<PropertyServiceDto> createProperty(PropertyServiceDto dto, List<MultipartFile> images) {
	    if (propertyRepository.existsByPropertyName(dto.getName())) {
	        return new APIResponse<>("Duplicate Property", 409, null);
	    }

	    Property property = new Property();
	    property.setPropertyName(dto.getName());
	    property.setNumberOfBathRooms(dto.getNumberOfBathrooms());
	    property.setNumberOfBeds(dto.getNumberOfBeds());
	    property.setNumberOfGuestAllowed(dto.getNumberOfGuestAllowed());
	    property.setNumberOfRooms(dto.getNumberOfRooms());
	    City cityName = cityRepository.findByCityName(dto.getCity());
	    property.setCity(cityName);
	    Area areaName = areaRepository.findByAreaName(dto.getArea());
	    property.setArea(areaName);
	    State stateName = stateRepository.findByStateName(dto.getState());
	    property.setState(stateName);

	    List<RoomsDto> roomsDtoList = dto.getRooms();
	    List<Rooms> roomEntities = new ArrayList<>();
	    for (RoomsDto roomDto : roomsDtoList) {
	        Rooms room = new Rooms();
	        room.setRoomType(roomDto.getRoomType());
	        room.setBasePrice(roomDto.getBasePrice());
	        room.setProperty(property);
	        roomEntities.add(room);
	    }

	    roomsRepository.saveAll(roomEntities);
	    Property savedProperty = propertyRepository.save(property);

	    if (images != null && !images.isEmpty()) {
	        for (MultipartFile imageFile : images) {
	            String stagingKey = "staging/" + UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
	            s3Service.uploadStaging(imageFile, stagingKey);
	            imageProcessingService.processImageAsync(imageFile, savedProperty);
	        }
	    }
	    
        //Send message to Kafka			      jwtfilter.username
//      EmailRequest req = new EmailRequest("shreyas200205@gmail.com", "Property added ok", "Property details are live");
//      kafkaTemplate.send(AppConstants.TOPIC, req);

	    return new APIResponse<>("Property created successfully", 201, dto);
	}


          

	public APIResponse<List<PropertyServiceDto>> searchProperty(String city, LocalDate date) {
	    List<Property> properties = propertyRepository.searchProperty(city, date);

	    List<PropertyServiceDto> dtoList = properties.stream().map(property -> {
	        PropertyServiceDto dto = new PropertyServiceDto();
	        dto.setId(property.getId());
	        dto.setName(property.getPropertyName());
	        dto.setNumberOfBeds(property.getNumberOfBeds());
	        dto.setNumberOfRooms(property.getNumberOfRooms());
	        dto.setNumberOfBathrooms(property.getNumberOfBathRooms());
	        dto.setNumberOfGuestAllowed(property.getNumberOfGuestAllowed());

	        dto.setCity(property.getCity() != null ? property.getCity().getCityName() : null);
	        dto.setState(property.getState() != null ? property.getState().getStateName() : null);
	        dto.setArea(property.getArea() != null ? property.getArea().getAreaName() : null);

	        List<RoomsDto> roomDtos = property.getRooms().stream().map(room -> {
	            RoomsDto rDto = new RoomsDto();
	            rDto.setId(room.getId());
	            rDto.setRoomType(room.getRoomType());
	            rDto.setBasePrice(room.getBasePrice());
	            return rDto;
	        }).toList();

	        dto.setRooms(roomDtos);

	        // Optional: Map image URLs
	        List<PropertyImagesDto> imageDtos = property.getImages().stream().map(img -> {
	            PropertyImagesDto imgDto = new PropertyImagesDto();
	            imgDto.setId(img.getId());
	            imgDto.setPropertyImageUrl(img.getPropertyImageUrl());
	            return imgDto;
	        }).toList();

	        dto.setImageUrls(imageDtos);

	        return dto;
	    }).toList();

	    APIResponse<List<PropertyServiceDto>> response = new APIResponse<>();
	    response.setStatus(200);
	    response.setMessage("Search result");
	    response.setData(dtoList);
	    return response;
	}
    public APIResponse<PropertyServiceDto> getPropertyById(Long id) {
        Optional<Property> propertyOpt = propertyRepository.findById(id);
        if (!propertyOpt.isPresent()) {
            return new APIResponse<>("Property not found", 404, null);
        }
        log.info("GetByProperty method initiated and property is found");
        Property property = propertyOpt.get();
        PropertyServiceDto dto = new PropertyServiceDto();
        dto.setId(property.getId());
        dto.setName(property.getPropertyName());
        dto.setCity(property.getCity().getCityName());
        dto.setState(property.getState().getStateName());
        dto.setArea(property.getArea().getAreaName());
        dto.setImageUrls(property.getImages().stream().map(img -> {
            PropertyImagesDto imgDto = new PropertyImagesDto();
            BeanUtils.copyProperties(img, imgDto);
            return imgDto;
        }).collect(Collectors.toList()));

        return new APIResponse<>("Property fetched", 200, dto);
    }

    public APIResponse<String> deleteProperty(Long id) {
        Optional<Property> propertyOpt = propertyRepository.findById(id);
        if (!propertyOpt.isPresent()) {
            return new APIResponse<>("Property not found", 404, null);
        }

        propertyRepository.delete(propertyOpt.get());
        return new APIResponse<>("Property deleted", 200, "Deleted Successfully");
    }

    // Helper method to check file extensions
    private boolean isImageFile(String filename) {
        if (filename == null) return false;
        String lowerCase = filename.toLowerCase();
        return lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".png");
    }


    


}
