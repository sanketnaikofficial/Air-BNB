package com.PaymentService.Client;

import org.springframework.cloud.openfeign.FeignClient;   
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.PaymentService.Config.FeignClientConfig;
import com.PaymentService.Dto.UserDto;




@FeignClient(name = "securityauthconfig",configuration = FeignClientConfig.class)
public interface AuthServiceFeignClient {

    @GetMapping("/api/v1/auth/get-user")
    UserDto getUserByUsername(@RequestParam("username") String username, 
    		@RequestHeader("Authorization") String token);
}
