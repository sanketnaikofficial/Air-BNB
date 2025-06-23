package com.PaymentService.Dto;

import lombok.Data;

@Data
public class UserDto {
	private Long id;
	private String name;
	private String username;
	private String email;
	private String password;
	private String role;

}
