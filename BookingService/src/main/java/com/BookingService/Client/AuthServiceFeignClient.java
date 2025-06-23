package com.BookingService.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.BookingService.Dto.UserDto;




@FeignClient(name = "securityauthconfig")
public interface AuthServiceFeignClient {

    @GetMapping("/api/v1/auth/get-user")
    UserDto getUserByUsername(@RequestParam("username") String username,
    		@RequestHeader("Authorization") String token);
}
