package com.PropertyService.Service;

import com.PropertyService.Dto.*; 
import com.PropertyService.Entity.*;
import com.PropertyService.Repository.PropertyRepository;
import com.PropertyService.Repository.RoomAvailabilityRepository;
import com.PropertyService.Repository.RoomsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomsService {
	@Autowired
	private final RoomAvailabilityRepository roomsAvailabilityRepository;
	
	@Autowired
    private final RoomsRepository roomsRepository;
	@Autowired
	private final PropertyRepository propertyRepository;

    public APIResponse<RoomsDto> createRoom(RoomsDto dto, Long propertyId) {
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        if (!propertyOpt.isPresent()) {
            return new APIResponse<>("Property not found", 404, null);
        }

        Rooms room = new Rooms();
        BeanUtils.copyProperties(dto, room);
        room.setProperty(propertyOpt.get());
        Rooms saved = roomsRepository.save(room);

        BeanUtils.copyProperties(saved, dto);
        return new APIResponse<>("Room created", 201, dto);
    }

    public APIResponse<List<RoomsDto>> getAllRooms() {
        List<RoomsDto> rooms = roomsRepository.findAll().stream().map(room -> {
            RoomsDto dto = new RoomsDto();
            BeanUtils.copyProperties(room, dto);
            return dto;
        }).collect(Collectors.toList());
        return new APIResponse<>("Rooms fetched", 200, rooms);
    }

    public APIResponse<String> deleteRoom(Long id) {
        Optional<Rooms> room = roomsRepository.findById(id);
        if (!room.isPresent()) {
            return new APIResponse<>("Room not found", 404, null);
        }
        roomsRepository.delete(room.get());
        return new APIResponse<>("Room deleted", 200, "Deleted successfully");
    }
    
    public List<RoomAvailabilityDto> getTotalRoomsAvailable(Long id) {
    
        List<RoomAvailability> roomAvailabilities = roomsAvailabilityRepository.findByRoom_Id(id);
     
        List<RoomAvailabilityDto> dtoList = roomAvailabilities.stream().map(roomAvailability -> {
            RoomAvailabilityDto dto = new RoomAvailabilityDto();
            dto.setId(roomAvailability.getId());
            dto.setAvailableDate(roomAvailability.getAvailableDate());
            dto.setAvailableCount(roomAvailability.getAvailableCount());
            return dto;
        }).toList();

        return dtoList;
    }

    public Rooms getRoomById(Long id) {
        return roomsRepository.getRoomById(id);
    }

	public List<Long> getRoomIdsByPropertyId(Long propertyId) {
		// TODO Auto-generated method stub
		List<Long> roomIds=roomsRepository.findRoomIdsByPropertyId(propertyId);
		return roomIds;
	}
}

