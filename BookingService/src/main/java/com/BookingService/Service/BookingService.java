package com.BookingService.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.ResourceAccessException;

import com.BookingService.Client.PropertyClient;
import com.BookingService.Dto.APIResponse;
import com.BookingService.Dto.BookingDto;
import com.BookingService.Dto.BookingResponseDto;
import com.BookingService.Dto.PropertyServiceDto;
import com.BookingService.Dto.RoomAvailabilityDto;
import com.BookingService.Dto.RoomsDto;
import com.BookingService.Entity.BookingDate;
import com.BookingService.Entity.Bookings;
import com.BookingService.Repository.BookingDateRepository;
import com.BookingService.Repository.BookingRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class BookingService {


	@Autowired
	private PropertyClient propertyClient;

	@Autowired
	private BookingDateRepository bookingDateRepository;
	@Autowired
	private BookingRepository bookingRepository;

	public APIResponse<BookingResponseDto> cart(@RequestBody BookingDto bookingDto){

		APIResponse<BookingResponseDto> apiResponse = new APIResponse<>();

		APIResponse<PropertyServiceDto> propertyResponse = propertyClient.getPropertyById(bookingDto.getPropertyId());

		APIResponse<RoomsDto> roomType = propertyClient.getRoomType(bookingDto.getRoomId());

		//APIResponse<List<RoomAvailabilityDto>> totalRoomsAvailable = propertyClient.getTotalRoomsAvailable(bookingDto.getRoomAvailabilityId());
		APIResponse<List<RoomAvailabilityDto>> totalRoomsAvailable = propertyClient.getAllAvailabilityByRoomId(bookingDto.getRoomId());

		List<RoomAvailabilityDto> availableRooms = totalRoomsAvailable.getData();

		//Logic to check available rooms based on date and count
		for (LocalDate date : bookingDto.getDate()) {
	        boolean isAvailable = availableRooms.stream()
	                .anyMatch(ra -> ra.getAvailableDate().equals(date) && ra.getAvailableCount() >= bookingDto.getNumberOfRooms());
	        if (!isAvailable) {
	            apiResponse.setMessage("Sold Out");
	            apiResponse.setStatus(500);
	            apiResponse.setData(null);
	            return apiResponse;
	        }
	    }

	    Bookings bookings = new Bookings();
	    bookings.setName(bookingDto.getName());
	    bookings.setEmail(bookingDto.getEmail());
	    bookings.setMobile(bookingDto.getMobile());
	    bookings.setPropertyName(propertyResponse.getData().getName());
	    bookings.setPropertyId(propertyResponse.getData().getId());
	    bookings.setStatus("pending");
	    bookings.setTotalPrice(roomType.getData().getBasePrice() * bookingDto.getTotalNights()* bookingDto.getNumberOfRooms());
	    bookings.setNumberOfRooms(bookingDto.getNumberOfRooms());
	    Bookings savedBooking = bookingRepository.save(bookings);

	    // Save booking dates
	    for (LocalDate date : bookingDto.getDate()) {
	        BookingDate bookingDate = new BookingDate();
	        bookingDate.setDate(date);
	        bookingDate.setBookings(savedBooking);
	        bookingDateRepository.save(bookingDate);
	    }

	    BookingResponseDto responseDto = new BookingResponseDto(savedBooking.getId(), savedBooking.getTotalPrice());
	    apiResponse.setMessage("Booking created successfully");
	    apiResponse.setStatus(200);
	    apiResponse.setData(responseDto);
	    return apiResponse;
	}

	public String updatePaymentStatus(Long bookingId) throws Exception {
		Boolean res=true;
	    Bookings booking = bookingRepository.findById(bookingId)
	            .orElseThrow(() -> new ResourceAccessException("Booking not found"));
	    if (!"pending".equalsIgnoreCase(booking.getStatus())) {
	        APIResponse<String> response = new APIResponse<>();
	        response.setStatus(200);
	        response.setMessage("Booking already processed");
	        response.setData(null);
	        return response.getMessage();
	    }

	    List<LocalDate> bookingDates = bookingDateRepository.findDateByBookingId(bookingId);
	    List<String> formattedDates = bookingDates.stream()
	        .map(date -> date.format(DateTimeFormatter.ISO_DATE))
	        .toList();
	    APIResponse<List<RoomAvailabilityDto>> availabilityResponse = propertyClient
	            .getRoomsIdBasedOnPropertyIdAndDate(booking.getPropertyId(), formattedDates);
	    List<RoomAvailabilityDto> allAvailableRooms = availabilityResponse.getData();
	    for (LocalDate bookingDate : bookingDates) {
	        int totalAvailableCount = allAvailableRooms.stream()
	                .filter(dto -> dto.getAvailableDate().equals(bookingDate))
	                .mapToInt(RoomAvailabilityDto::getAvailableCount)
	                .sum();

	        if (totalAvailableCount < booking.getNumberOfRooms()) {
	        	return "Rooms not available on {}" +bookingDate;

	        }
	    }
	    for (LocalDate bookingDate : bookingDates) {
	        List<RoomAvailabilityDto> availableRooms = allAvailableRooms.stream()
	                .filter(dto -> dto.getAvailableDate().equals(bookingDate))
	                .sorted(Comparator.comparingInt(RoomAvailabilityDto::getAvailableCount).reversed())
	                .collect(Collectors.toList());

	        int roomsNeeded = booking.getNumberOfRooms();

	        for (RoomAvailabilityDto dto : availableRooms) {
	            if (roomsNeeded <= 0) {
					break;
				}

	            int toDecrement = Math.min(roomsNeeded, dto.getAvailableCount());

	            for (int i = 0; i < toDecrement; i++) {
	            	String dateStr = bookingDate.toString(); // ISO format (e.g., 2025-06-19)
	            	 res=propertyClient.decrementRoomAvailability(dto.getRoomId(), dateStr);

	            }

	            roomsNeeded -= toDecrement;
	        }
	        if(!res) {

	        	return "Rooms not available on " +bookingDate;
	        }

	        if (roomsNeeded > 0) {

	            throw new Exception("Unable to allocate required rooms on date: " + bookingDate);

	        }

	    }

	    // Mark booking as confirmed
	    booking.setStatus("booked");
	    bookingRepository.save(booking);
	    APIResponse<String> responseSuccess = new APIResponse<>();
	    responseSuccess.setStatus(200);
	    responseSuccess.setMessage("Booking confirmed and room availability updated");
	    responseSuccess.setData(null);
	    log.info(responseSuccess.getMessage());

	    return responseSuccess.getMessage();

}

	public APIResponse<BookingDto> getBookingDetailsByBookingId(Long id) {
		APIResponse response=new APIResponse();
		Optional<Bookings> op=bookingRepository.findById(id);
		 if (op.isPresent()) {
		        Bookings booking = op.get();
		        BookingDto dto = new BookingDto();
		        dto.setId(booking.getId());
		        dto.setName(booking.getName());
		        dto.setEmail(booking.getEmail());
		        dto.setMobile(booking.getMobile());
		        dto.setPropertyName(booking.getPropertyName());
		        dto.setTotalPrice(booking.getTotalPrice());
		        dto.setStatus(booking.getStatus());
		        dto.setNumberOfRooms(booking.getNumberOfRooms());
		        dto.setPropertyId(booking.getPropertyId());
		        response.setData(dto);
		        response.setMessage("fetched");
		        response.setStatus(200);
		        return response;
		    }
		 response.setData(null);
		 response.setMessage("Booking id not found");
		 response.setStatus(403);
		 return response;
}
}
