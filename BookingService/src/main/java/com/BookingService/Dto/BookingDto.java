package com.BookingService.Dto;


import java.time.LocalDate;
import java.util.List;

import com.BookingService.Entity.BookingDate;

import lombok.Data;

@Data
public class BookingDto {
	private Long id;
	private Long propertyId;
	private Long roomId;
	private String propertyName;
	private String name;
	private String email;
	private String mobile;
	private String status;
	private Integer roomAvailabilityId;
	private Double price;
	private String roomType;
	private BookingDate bookingDate;
	private Integer totalNights;
	private Double totalPrice;
	private Integer numberOfRooms;
	private List<LocalDate> date;
}
