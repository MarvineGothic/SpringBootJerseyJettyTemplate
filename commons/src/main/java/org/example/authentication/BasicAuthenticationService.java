package org.example.authentication;

public interface BasicAuthenticationService {
    String generateToken(String username, String password);
    AuthenticatedUser authenticate(String token);
}
