package com.SecurityAuthConfig.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class MessageController {
	//http://localhost:8080/api/v1/auth/getHello
	@GetMapping("/getHello")
	public String getHello() {
		return "Hello";
	}
	
	//http://localhost:8080/api/v1/auth/getWelcome
	@GetMapping("/getWelcome")
	public String getWelcome() {
		return "Welcome";
	}

}
