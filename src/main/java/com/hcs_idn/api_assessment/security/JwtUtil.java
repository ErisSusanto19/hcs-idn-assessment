package com.hcs_idn.api_assessment.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtUtil {
    @Value("${app.jwt-key}")
    private String jwtKey;

    @Value("${app.jwt-key}")
    private long jwtExpiration;


    public String generateToken(Authentication auth, UUID id){
        UserDetails userPrinciple = (UserDetails) auth.getPrincipal();

        //Extract roles from UserDetails
        List<String> roles = userPrinciple.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();

        return JWT.create()
                .withSubject(userPrinciple.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + jwtExpiration))
                .withClaim("id", id.toString())
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(jwtKey));
    }

    public boolean validateToken(String token){
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtKey)).build();
            verifier.verify(token);
            return true;
        } catch(JWTVerificationException e) {
            System.out.print("Invalid JWT Token: {}" + e.getMessage());
            return false;
        }
    }

    public String getUsername(String token){
        DecodedJWT decodedJwt = JWT.decode(token);

        return decodedJwt.getSubject();
    }

    public String getUserId(String token){
        DecodedJWT decodedJwt = JWT.decode(token);

        return decodedJwt.getClaim("id").asString();
    }
}
