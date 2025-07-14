package org.example.infrastructure.identity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.error.ServiceException;
import org.example.infrastructure.datasource.entity.RefreshToken;
import org.example.infrastructure.datasource.entity.UserEntity;
import org.example.infrastructure.datasource.repository.RefreshTokenJpaRepository;
import org.example.infrastructure.datasource.repository.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtRefreshTokenService implements RefreshTokenService {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean validateRefreshToken(String userHandle) {
        return refreshTokenJpaRepository.findByUserHandleAndRevokedIsFalse(userHandle).stream().anyMatch(
                this::isRefreshTokenValid
        );
    }

    @Override
    @Transactional
    public boolean rotateRefreshToken(String token, String oldToken) {
        var oldRefreshToken = refreshTokenJpaRepository.findByToken(oldToken).orElse(null);
        if (oldRefreshToken == null || oldRefreshToken.isExpired()) {
            log.error("\nRefreshToken null or expired {}", oldRefreshToken);
            return false;
        }
        if (oldRefreshToken.isRevoked()) {
            revokeAllUserTokens(oldRefreshToken.getUser().getHandle());
            log.error("\nRevoked all user tokens");
            return false;
        }
        var newRefreshToken = createNewRefreshToken(oldRefreshToken.getUser(), token);
        oldRefreshToken.setReplacedBy(newRefreshToken);
        oldRefreshToken.setRevoked(true);

        refreshTokenJpaRepository.save(oldRefreshToken);
        refreshTokenJpaRepository.save(newRefreshToken);

        return true;
    }

    @Override
    public void saveRefreshToken(String userHandle, String refreshToken) {
        var user = userJpaRepository.findByHandle(userHandle).orElseThrow(() -> new ServiceException("Bad credentials", HttpStatus.BAD_REQUEST.value()));
        var token = createNewRefreshToken(user, refreshToken);
        refreshTokenJpaRepository.save(token);
    }

    private void revokeAllUserTokens(String userHandle) {
        List<RefreshToken> tokens = refreshTokenJpaRepository.findAllByUserHandle(userHandle);
        tokens.forEach(t -> t.setRevoked(true));
        refreshTokenJpaRepository.saveAll(tokens);
    }

    private RefreshToken createNewRefreshToken(UserEntity user, String token) {
        return RefreshToken.builder()
                .user(user)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofDays(7)))
                .token(token)
                .revoked(false)
                .build();
    }

    public boolean isRefreshTokenValid(RefreshToken refreshToken) {
        return refreshToken != null && !refreshToken.isRevoked();
    }
}
