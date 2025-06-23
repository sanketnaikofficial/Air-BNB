package com.Microservices_01.config;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Microservices_01.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class Config {
    
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
        "/micro1/message",
        "/message"
    };

    @Bean
    public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
        
        http.authorizeRequests(req -> {
            req.requestMatchers(publicEndpoints)
                .permitAll()
                .anyRequest().permitAll();
        })
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        
        return http.csrf().disable().build();
    }
    
}
