package com.PaymentService.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.PaymentService.Dto.EmailKafkaMessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, EmailKafkaMessageDto> kafkaTemplate;

    @Value("${email.kafka.topic}")
    private String topic;

    public void sendBookingEmail(EmailKafkaMessageDto message) {
        kafkaTemplate.send(topic, message);
    }
}
