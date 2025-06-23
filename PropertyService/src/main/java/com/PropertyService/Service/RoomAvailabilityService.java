package com.PropertyService.Service;

import com.PropertyService.Dto.*;
import com.PropertyService.Entity.*;
import com.PropertyService.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomAvailabilityService {

	@Autowired
    private final RoomAvailabilityRepository roomAvailabilityRepository;
	@Autowired
	private final RoomsRepository roomsRepository;

    public APIResponse<RoomAvailabilityDto> createAvailability(RoomAvailabilityDto dto, Long roomId) {
        Optional<Rooms> roomOpt = roomsRepository.findById(roomId);
        if (!roomOpt.isPresent()) {
            return new APIResponse<>("Room not found", 404, null);
        }

        RoomAvailability availability = new RoomAvailability();
        availability.setAvailableCount(dto.getAvailableCount());
        availability.setRoom(roomOpt.get());
        availability.setAvailableDate(dto.getAvailableDate());
        availability = roomAvailabilityRepository.save(availability);
        dto.setId(availability.getId());
        dto.setRoomId(roomId);
        
        return new APIResponse<>("Availability created", 201, dto);
    }

    public APIResponse<List<RoomAvailabilityDto>> getAllAvailability() {
        List<RoomAvailabilityDto> list = roomAvailabilityRepository.findAll().stream().map(avail -> {
            RoomAvailabilityDto dto = new RoomAvailabilityDto();
            BeanUtils.copyProperties(avail, dto);
            return dto;
        }).collect(Collectors.toList());
        return new APIResponse<>("Availabilities fetched", 200, list);
    }

    public APIResponse<String> deleteAvailability(Long id) {
        Optional<RoomAvailability> opt = roomAvailabilityRepository.findById(id);
        if (!opt.isPresent()) {
            return new APIResponse<>("Availability not found", 404, null);
        }
        roomAvailabilityRepository.delete(opt.get());
        return new APIResponse<>("Deleted", 200, "Deleted successfully");
    }
    public APIResponse<List<RoomAvailabilityDto>> getAvailabilityByRoomId(Long roomId) {
        List<RoomAvailability> availabilities = roomAvailabilityRepository.findByRoom_Id(roomId);
        List<RoomAvailabilityDto> dtos = availabilities.stream().map(this::convertToDto).toList();
        APIResponse<List<RoomAvailabilityDto>> response = new APIResponse<>();
        response.setMessage("Availability fetched");
        response.setStatus(200);
        response.setData(dtos);
        return response;
    }
    private RoomAvailabilityDto convertToDto(RoomAvailability entity) {
        RoomAvailabilityDto dto = new RoomAvailabilityDto();
        dto.setId(entity.getId());
        dto.setAvailableDate(entity.getAvailableDate());
        dto.setAvailableCount(entity.getAvailableCount());
        return dto;
    }
    
    public Boolean decrementRoomAvailability(long roomId,LocalDate date) {
    	 Boolean res=true;
    	 RoomAvailability availability = roomAvailabilityRepository.findByRoomIdAndDate(roomId, date);
         if (availability != null && availability.getAvailableCount() > 0) {
             availability.setAvailableCount(availability.getAvailableCount() - 1);
             roomAvailabilityRepository.save(availability);
             System.out.println("CountDecremented Successfully");
             return res;
         }else
        	 res=false;
         return res;
    }
    
    public APIResponse<List<RoomAvailabilityDto>> getAllAvailabilityByRoomIdAndDate(Long propertyId, List<LocalDate> dates) {
        List<Long> roomIds = roomsRepository.findRoomIdsByPropertyId(propertyId);
        List<RoomAvailabilityDto> dtoList = new ArrayList<>();

        for (Long roomId : roomIds) {
            for (LocalDate date : dates) {
                List<RoomAvailability> availabilityList =
                        roomAvailabilityRepository.findAllAvailabilityByRoomIdAndDate(roomId, date);

                for (RoomAvailability availability : availabilityList) {
                    RoomAvailabilityDto dto = new RoomAvailabilityDto();
                    dto.setId(availability.getId());
                    dto.setAvailableDate(availability.getAvailableDate());
                    dto.setAvailableCount(availability.getAvailableCount());
                    dto.setRoomId(roomId);
                    dtoList.add(dto);
                }
            }
        }

        return new APIResponse<>("Rooms Available", 200, dtoList);
    }

}

