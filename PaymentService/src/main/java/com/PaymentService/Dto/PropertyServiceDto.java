package com.PaymentService.Dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyServiceDto {
	private Long id;
	private String name;
	private Integer numberOfBeds;
	private Integer numberOfRooms;
	private Integer numberOfBathrooms;
	private Integer numberOfGuestAllowed;
	private String city;
	private String area;
	private String state;
	private List<RoomsDto> rooms;
	


	

}
