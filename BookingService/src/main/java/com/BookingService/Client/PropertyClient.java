package com.BookingService.Client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.BookingService.Dto.APIResponse;
import com.BookingService.Dto.PropertyServiceDto;
import com.BookingService.Dto.RoomAvailabilityDto;
import com.BookingService.Dto.RoomsDto;

@FeignClient(name = "propertyservice")
public interface PropertyClient {

	@GetMapping("/api/v1/property/get-property-id")
	public APIResponse<PropertyServiceDto> getPropertyById(@RequestParam("id") Long id);

	@GetMapping("/api/v1/property/rooms/room-available-room-id")
	public APIResponse<List<RoomAvailabilityDto>> getTotalRoomsAvailable(@RequestParam("id") Long id);


	@GetMapping("/api/v1/property/rooms/get-by-room-id")
	public APIResponse<RoomsDto> getRoomType(@RequestParam("id") Long id);

	@GetMapping("/api/v1/property/room-availability/get-all-availability-by-room-id")
	public APIResponse<List<RoomAvailabilityDto>> getAllAvailabilityByRoomId(@RequestParam("roomId") Long roomId);

	@GetMapping("/api/v1/property/room-availability/get-all-availability-by-property-id-and-date")
	APIResponse<List<RoomAvailabilityDto>> getRoomsIdBasedOnPropertyIdAndDate(
	    @RequestParam("propertyId") Long propertyId,
	    @RequestParam("date") List<String> dates
	);


	@PutMapping("/api/v1/property/room-availability/decrement-room-availability")
	public Boolean decrementRoomAvailability(@RequestParam("roomId") Long roomId,
			 @RequestParam("date") String date);

	@PutMapping("/api/v1/property/room-availability/update")
	public APIResponse<String> updateAvailabilityCount(@RequestBody RoomAvailabilityDto dto);

	@GetMapping("/api/v1/property/room-availability/rooms/get-rooms-id-by-property-id")
    APIResponse<List<Long>>getRoomsIdBasedOnPropertyId(@RequestParam("propertyId") Long propertyId);

}

