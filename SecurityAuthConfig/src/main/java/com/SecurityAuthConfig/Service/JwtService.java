package com.SecurityAuthConfig.Service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
@Service
public class JwtService {
	
	//Injects the value of jwt.<name> from the configuration or 
   //              app.properties fie into the variables.
	    @Value("${jwt.key}")
	    private String secretKey;
	    @Value("${jwt.issuer}")
	    private String issuer;
	    @Value("${jwt.expiration}")
	    private int expiration;

	//Generates the JWT token
    public String generateToken(String username, String role) {
        return JWT.create()
            .withSubject(username)
            .withClaim("role", role)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
            .sign(Algorithm.HMAC256(secretKey));
    }

    //Validates the Token
    public String validateTokenAndRetrieveSubject(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
            .build()
            .verify(token)
            .getSubject();
    }
}
