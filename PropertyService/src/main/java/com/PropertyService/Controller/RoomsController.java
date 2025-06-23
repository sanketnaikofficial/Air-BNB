package com.PropertyService.Controller;

import com.PropertyService.Dto.*;
import com.PropertyService.Entity.Rooms;
import com.PropertyService.Service.RoomsService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property/rooms")
@RequiredArgsConstructor
public class RoomsController {
	
	@Autowired
	private final RoomsService roomsService;

    @PostMapping("/add-room/{propertyId}")
    public ResponseEntity<APIResponse<RoomsDto>> addRoom(@RequestBody RoomsDto dto, @PathVariable Long propertyId) {
        return ResponseEntity.ok(roomsService.createRoom(dto, propertyId));
    }

    @GetMapping("/get-all-rooms-id")
    public ResponseEntity<APIResponse<List<RoomsDto>>> getAllRooms() {
        return ResponseEntity.ok(roomsService.getAllRooms());
    }

    @DeleteMapping("/delete-room/{id}")
    public ResponseEntity<APIResponse<String>> deleteRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomsService.deleteRoom(id));
    }
    
    @GetMapping("/get-by-room-id")
	public APIResponse<Rooms> getRoomType(@RequestParam Long id){
		Rooms room = roomsService.getRoomById(id);
		
		APIResponse<Rooms> response = new APIResponse<>();
	    response.setMessage("Total rooms");
	    response.setStatus(200);
	    response.setData(room);
	    return response;
	}
    
    @GetMapping("/room-available-room-id")
	public APIResponse<List<RoomAvailabilityDto>> getTotalRoomsAvailable(@RequestParam Long id){
		List<RoomAvailabilityDto> totalRooms = roomsService.getTotalRoomsAvailable(id);
		
		APIResponse<List<RoomAvailabilityDto>> response = new APIResponse<>();
	    response.setMessage("Total rooms");
	    response.setStatus(200);
	    response.setData(totalRooms);
	    return response;
	}
    
    @GetMapping("/get-rooms-id-by-property-id")
    public APIResponse<List<Long>> getRoomsIdByPropertyId(@RequestParam Long propertyId) {
        List<Long> roomIds = roomsService.getRoomIdsByPropertyId(propertyId);

        APIResponse<List<Long>> response = new APIResponse<>();
        response.setStatus(200);
        response.setMessage("Room IDs fetched successfully");
        response.setData(roomIds);

        return response;
    }
}
