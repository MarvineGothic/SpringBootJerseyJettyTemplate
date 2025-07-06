package org.example.authentication;

public interface BasicAuthenticationService {
    String getBasicAuthenticationToken(String username, String password);
    UserSecurityContext validateBasicAuthenticationToken(String token);
}
