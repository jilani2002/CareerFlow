package com.careerflow.security.impl;

import com.careerflow.security.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;
    private final Long expiration;

    public  JwtServiceImpl(@Value("${jwt.secret}") String secretKey,
                           @Value("${jwt.expiration}") Long expiration){
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    @Override
    public String generateToken(String email) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);
        return Jwts
                .builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String extractEmail(String token) {

        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public boolean isTokenValid(String token, String email) {
        try {
            var claims = Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String extractedEmail = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            return extractedEmail.equals(email) && expirationDate.after(new Date());

        } catch (Exception exception) {
            return false;
        }
    }
}