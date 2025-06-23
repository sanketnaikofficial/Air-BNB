package com.PropertyService.Constants;

import org.springframework.beans.factory.annotation.Value;

public class AppConstants {
	
	public static final String TOPIC = "send-email";
	
	@Value("${kafka.host}")
	public static String KAFKA_HOST;
}
