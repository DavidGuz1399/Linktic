package com.inventory.intentory_service.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "s3cretKey";
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String generateToken(String username) {
        return  JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        return JWT.require(algorithm).build().verify(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}