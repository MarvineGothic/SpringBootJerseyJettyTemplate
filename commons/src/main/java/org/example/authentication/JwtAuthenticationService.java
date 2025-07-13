package org.example.authentication;

public interface JwtAuthenticationService {
    String generateAccessToken(String userName, String email, String role);
    String generateRefreshToken(String userName, String email, String role);
    AuthUser authenticateAccessToken(String token);
    AuthUser authenticateRefreshToken(String token);
}
