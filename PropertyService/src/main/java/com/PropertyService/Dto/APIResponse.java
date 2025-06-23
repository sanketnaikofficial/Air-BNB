package com.PropertyService.Dto;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class APIResponse<T> {
	private String message;
	private Integer status;
	private T data;

}
