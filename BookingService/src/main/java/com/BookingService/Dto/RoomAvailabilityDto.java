package com.BookingService.Dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomAvailabilityDto {
	private Long id;
	private LocalDate availableDate;
	private Integer availableCount;
	private Long roomId;


}
