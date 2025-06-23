package com.PaymentService.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.PaymentService.Config.FeignClientConfig;
import com.PaymentService.Dto.APIResponse;
import com.PaymentService.Dto.PropertyServiceDto;



@FeignClient(name = "propertyservice",configuration = FeignClientConfig.class) 
public interface PropertyClient {
	
	@GetMapping("/api/v1/property/get-property-id")
	public APIResponse<PropertyServiceDto> getPropertyById(@RequestParam("id") Long id);
	
	
}
