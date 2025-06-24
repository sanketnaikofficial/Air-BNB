package com.NotificationService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailKafkaMessageDto {

	
	    private String toEmail;
	    private String subject;
	    private String body;
	    private byte[] pdfAttachment; // PDF as byte array
	
}
