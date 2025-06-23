package com.PaymentService.Client;

import org.springframework.cloud.openfeign.FeignClient ;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.PaymentService.Config.FeignClientConfig;
import com.PaymentService.Dto.APIResponse;
import com.PaymentService.Dto.BookingDto;

@FeignClient(name = "bookingservice",configuration = FeignClientConfig.class) 
public interface BookingClient {
	
	@PutMapping("/api/v1/booking/update-payment-status")
    String updatePaymentStatus(@RequestParam("bookingId") Long bookingId);
	
	
	@GetMapping("/api/v1/booking/get-booking-details-by-id")
	public APIResponse<BookingDto> getBookingDetialsById(@RequestParam("id") Long id);

}
