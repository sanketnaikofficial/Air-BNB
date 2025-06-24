package com.NotificationServiceConfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
@Component
public class MailConfigVerifier {

    @Value("${spring.mail.host:NOT-FOUND}")
    private String host;

    @Value("${spring.mail.username:NOT-FOUND}")
    private String username;

    @PostConstruct
    public void verify() {
        System.out.println("Mail Host: " + host);
        System.out.println("Mail Username: " + username);
    }
}


