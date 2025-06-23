package com.Api_gateway.Filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
	
	 @Value("${jwt.key}")
	 private String injectedKey;
	 private static String key;
	 
	@PostConstruct
	public void init() {
	    key = injectedKey; // ← happens after constructor, too late for `final`
	}
    private static final List<String> openApiEndpoints = List.of(
            //Swaggers URLs
     		"/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            
            //BookingService URLs
            "/bookingservice/",
            
            //PropertyService URLs
            "/propertyservice/",          
                     
            //SecurityAuthService URLs
            "/authservice/",
            
            //PaymentService URLs
            "/paymentservice/",            
            
            // microservices 01
            "/micro1/"
                
    );

    private static final Map<String, List<String>> protectedEndpointsWithRoles = Map.of(
    	    "/micro1/message1", List.of("ROLE_ADMIN")
    	);


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();

        // Allow public endpoints
        if (isPublicEndpoint(requestPath)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(key))
                    .build()
                    .verify(token);

            String role = jwt.getClaim("role").asString();

            System.out.println("Request path: " + requestPath);
            System.out.println("Role from token: " + role);

            if (!isAuthorized(requestPath, role)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // Pass role to downstream services (optional)
            exchange = exchange.mutate()
                    .request(r -> r.header("X-User-Role", role))
                    .build();

        } catch (JWTVerificationException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private boolean isPublicEndpoint(String path) {
    	return openApiEndpoints.stream().anyMatch(path::startsWith);
    }

    private boolean isAuthorized(String path, String role) {
        for (Map.Entry<String, List<String>> entry : protectedEndpointsWithRoles.entrySet()) {
            String protectedPath = entry.getKey();
            List<String> allowedRoles = entry.getValue();

            if (path.startsWith(protectedPath)) {
                System.out.println("Matched protected path: " + protectedPath + " | Allowed roles: " + allowedRoles);
                return allowedRoles.contains(role);
            }
        }
        return true; // Allow access if path is not protected (can be changed to false to deny by default)
    }
    @Override
    public int getOrder() {
        return -1;
    }
}
