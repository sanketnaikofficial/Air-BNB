package com.NotificationService.Service;

import com.NotificationService.AppConstants.AppConstants;
import com.NotificationService.Dto.EmailKafkaMessageDto;
import com.NotificationService.Dto.EmailRequest;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailRequestListener {
	   @Autowired
	    private JavaMailSender javaMailSender;

	    @KafkaListener(topics = AppConstants.TOPIC, groupId = "send-email-group", containerFactory = "kafkaListenerContainerFactory")
	    public void consumeMessage(EmailKafkaMessageDto message) {
	        System.out.println("Received Kafka message with PDF");

	        try {
	            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

	            helper.setTo(message.getToEmail());
	            helper.setSubject(message.getSubject());
	            helper.setText(message.getBody(), true); // true = HTML

	            // Attach PDF
	            if (message.getPdfAttachment() != null && message.getPdfAttachment().length > 0) {
	                helper.addAttachment("Booking_Confirmation.pdf", new ByteArrayResource(message.getPdfAttachment()));
	            }

	            javaMailSender.send(mimeMessage);
	            System.out.println("Email with PDF sent successfully to: " + message.getToEmail());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}