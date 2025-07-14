package org.example.infrastructure.identity;

public interface RefreshTokenService {
    boolean validateRefreshToken(String userHandle);
    boolean rotateRefreshToken(String token, String oldToken);
    void saveRefreshToken(String userHandle, String refreshToken);
}
