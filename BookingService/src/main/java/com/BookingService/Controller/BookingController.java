package com.BookingService.Controller;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BookingService.Dto.APIResponse;
import com.BookingService.Dto.BookingDto;
import com.BookingService.Dto.BookingResponseDto;
import com.BookingService.Service.BookingService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@PostMapping("/add-to-cart")
	public ResponseEntity<APIResponse<BookingResponseDto>> cart(@RequestBody BookingDto bookingDto) {
	    APIResponse<BookingResponseDto> response = bookingService.cart(bookingDto);
	    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@Transactional
	@PutMapping("/update-payment-status")
	public synchronized String updatePaymentStatus(@RequestParam("bookingId") Long bookingId) throws Exception {
	     String res=bookingService.updatePaymentStatus(bookingId);
	     return res;
	}
	
	@GetMapping("/get-booking-details-by-id")
	public APIResponse <BookingDto> getBookingDetialsById(@RequestParam Long id){
		
		return bookingService.getBookingDetailsByBookingId(id);
	}
}




