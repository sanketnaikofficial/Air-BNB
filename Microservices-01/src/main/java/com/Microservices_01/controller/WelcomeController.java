package com.Microservices_01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
	
	@GetMapping("/message")
	public String getMessage() {
		return "welcome";
	}

}
