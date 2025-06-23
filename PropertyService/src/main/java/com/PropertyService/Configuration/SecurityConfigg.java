package com.PropertyService.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.PropertyService.filter.JwtFilter;


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
    		"/api/v1/property/area/get-area/{id}",
    		"/api/v1/property/area/get-all-areas",
    		"/api/v1/property/city/get-by-id/{id}",
    		"/api/v1/property/city/getAll",
    		"/api/v1/property/state/get-state/{id}",
    		"/api/v1/property/state/get-all-state",
    		"/api/v1/property/rooms/get-all-rooms-id",
    		"/api/v1/property/rooms/get-by-room-id",
    		"/api/v1/property/rooms/room-available-room-id",
    		"/api/v1/property/rooms/get-rooms-id-by-property-id",
    		"/api/v1/property/room-availability/get",
    		"/api/v1/property/room-availability/decrement-room-availability",
            "/api/v1/property/room-availability/get-all-availability-by-property-id-and-date",
            "/api/v1/property/create-property-without-images",
            "/api/v1/property/create-property-with-images",
            "/api/v1/property/search-property",
            "/api/v1/property/get-property-id",	
            
            
    		
    };
    
    String[] adminSpecificEndpoints = {
    		"/api/v1/property/area/create",
    		"/api/v1/property/area/delete/{id}",
    		"/api/v1/property/city/create",
    		"/api/v1/property/city/delete-by-id/{id}",
    		"/api/v1/property/state/create-state",
    		"/api/v1/property/state/add-room/{propertyId}",
    		"/api/v1/property/rooms/delete-room/{id}",
    		"/api/v1/property/rooms/delete-state/{id}",
    		"/api/v1/property/room-availability/add/{roomId}",
    		"/api/v1/property/room-availability/delete/{id}",
    		"/api/v1/property/delete/{id}",
    		
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