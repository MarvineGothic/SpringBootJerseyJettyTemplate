package org.example.authentication;

public interface BasicAuthenticationService {
    String generateToken(String username, String password);
    AuthUser authenticate(String token);
}
