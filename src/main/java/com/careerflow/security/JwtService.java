package com.careerflow.security;

public interface JwtService {

    String generateToken(String email);
    String extractEmail(String token);
    boolean isTokenValid(String token, String email);
}
