package com.PaymentService.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.PaymentService.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigg {
    
    @Autowired
    private JwtFilter filter;

    String[] publicEndpoints = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**",
        "/actuator/**", 
        "/eureka/**",
     };
    
    String[] customerSpecificEndpoints = {
    		"/product/v1/payment/checkout",
    		"/product/v1/payment/success/**",
    		"product/v1/payment/cancel/**",
    		
    };
    
    String[] adminSpecificEndpoints = {

    		
    };

    @Bean
    public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
        
        http.authorizeRequests(req -> {
            req.requestMatchers(adminSpecificEndpoints).hasRole("ADMIN")
            .requestMatchers(customerSpecificEndpoints).hasAnyRole("ADMIN", "CUSTOMER")
            .anyRequest().authenticated();
         
        })
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        
        return http.csrf().disable().build();
    }
}