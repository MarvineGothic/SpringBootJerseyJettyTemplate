package org.example.authentication;

public interface JwtAuthenticationService {
    String generateJwt(String userName, String email, String role);
    UserJwtPayload parseJwt(String jwt);
}
