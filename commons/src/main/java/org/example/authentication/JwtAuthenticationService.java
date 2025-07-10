package org.example.authentication;

public interface JwtAuthenticationService {
    String generateToken(String userName, String email, String role);
    AuthenticatedUser authenticate(String token);
}
