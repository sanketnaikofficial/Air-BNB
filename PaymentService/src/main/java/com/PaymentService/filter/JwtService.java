package com.PaymentService.filter;

import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

	 @Value("${jwt.key}")
	 private String injectedKey;
	 private static String key;
	 
	@PostConstruct
	public void init() {
	    key = injectedKey; // ← happens after constructor, too late for `final`
	}

    public String validateTokenAndRetrieveSubject(String token) {
        return JWT.require(Algorithm.HMAC256(key))
            .build()
            .verify(token)
            .getSubject();
    }
}
