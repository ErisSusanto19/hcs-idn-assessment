package com.hcs_idn.api_assessment.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hcs_idn.api_assessment.entities.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class JwtUtil {
    @Value("${app.jwt-key}")
    private String jwtKey;

    @Value("${app.jwt-expiration}")
    private long jwtExpiration;


    public String generateToken(Authentication auth){
        Account userPrinciple = (Account) auth.getPrincipal();

        UUID userId = userPrinciple.getId();

        //Extract roles from Account
        List<String> roles = userPrinciple.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();

        return JWT.create()
                .withSubject(userPrinciple.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + jwtExpiration))
                .withClaim("id", userId.toString())
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(jwtKey));
    }

    public boolean validateToken(String token){
        return verifyToken(token) != null;
    }

    private DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtKey)).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT Token: {}", e.getMessage());
            return null;
        }
    }

    public String getUsername(String token){
        DecodedJWT decodedJWT = verifyToken(token);
        if (decodedJWT != null) {
            return decodedJWT.getSubject();
        }
        return null;
    }

    public String getUserId(String token){
        DecodedJWT decodedJwt = verifyToken(token);

        return decodedJwt.getClaim("id").asString();
    }
}
