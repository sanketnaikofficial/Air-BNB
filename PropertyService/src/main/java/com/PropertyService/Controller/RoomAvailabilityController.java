package com.PropertyService.Controller;

import com.PropertyService.Dto.*;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.PropertyService.Repository.RoomAvailabilityRepository;
import com.PropertyService.Repository.RoomsRepository;
import com.PropertyService.Service.RoomAvailabilityService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/property/room-availability")
@RequiredArgsConstructor
public class RoomAvailabilityController {
	
	@Autowired
    private final RoomAvailabilityService roomAvailabilityService;
	
	@Autowired
    private final RoomAvailabilityRepository roomAvailabilityRepository;
	
	@Autowired
	private final RoomsRepository roomsRepository;
    

    @PostMapping("/add/{roomId}")
    public ResponseEntity<APIResponse<RoomAvailabilityDto>> createAvailability(@RequestBody RoomAvailabilityDto dto, @PathVariable Long roomId) {
        return ResponseEntity.ok(roomAvailabilityService.createAvailability(dto, roomId));
    }

    @GetMapping("/get")
    public ResponseEntity<APIResponse<List<RoomAvailabilityDto>>> getAllAvailability() {
        return ResponseEntity.ok(roomAvailabilityService.getAllAvailability());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse<String>> deleteAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(roomAvailabilityService.deleteAvailability(id));
    }
    @GetMapping("/get-all-availability-by-room-id")
    public ResponseEntity<APIResponse<List<RoomAvailabilityDto>>> getAvailabilityByRoomId(@RequestParam Long roomId) {
        return ResponseEntity.ok(roomAvailabilityService.getAvailabilityByRoomId(roomId));
    }
    @PutMapping("/decrement-room-availability")
    public Boolean decrementRoomAvailability(@RequestParam Long roomId, @RequestParam LocalDate date) {
    	 Boolean res=roomAvailabilityService.decrementRoomAvailability(roomId, date);
    	 return res;
  
    }
    @GetMapping("/get-all-availability-by-property-id-and-date")
    public ResponseEntity<APIResponse<List<RoomAvailabilityDto>>> getAllAvailabilityByRoomIdAndDate(
        @RequestParam Long propertyId,
        @RequestParam("date") List<String> dateStrings) {

        List<LocalDate> dates = dateStrings.stream()
            .map(dateStr -> LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE))
            .collect(Collectors.toList());

        APIResponse<List<RoomAvailabilityDto>> dto =
            roomAvailabilityService.getAllAvailabilityByRoomIdAndDate(propertyId, dates);
        return ResponseEntity.ok(dto);
    }

}
