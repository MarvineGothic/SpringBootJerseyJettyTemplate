package org.example.authentication;

import lombok.Builder;

public interface JwtAuthenticationService {
    AuthUser authenticateAccessToken(String token);
    TokenSet rotateRefreshToken(String oldToken);
    TokenSet generateTokenSet(String handle, String email, String role);

    @Builder
    record TokenSet(String accessToken, String refreshToken) {}
}
