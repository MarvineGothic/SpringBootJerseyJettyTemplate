package org.example.infrastructure.identity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.AuthUser;
import org.example.authentication.JwtAuthenticationService;
import org.example.error.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserJwtAuthenticationService implements JwtAuthenticationService {
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpirationSeconds;

    @Value("${jwt.refresh.expiration}")
    private String refreshTokenExpirationSeconds;

    @Value("${spring.application.name}")
    private String issuer;

    private SecretKey accessKey;
    private SecretKey refreshKey;

    @PostConstruct
    public void init() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecret);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecret);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    @Override
    public TokenSet generateTokenSet(String handle, String email, String role) {
        var accessToken = generateAccessToken(handle, email, role);
        var refreshToken = generateRefreshToken(handle, email, role);

        refreshTokenService.saveRefreshToken(handle, refreshToken);

        return TokenSet.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthUser authenticateAccessToken(String token) {
        AuthUser authUser;
        try {
            authUser = authenticateToken(token, accessKey);
            if (!refreshTokenService.validateRefreshToken(authUser.handle())) {
                return null;
            }
        } catch (ServiceException e) {
            return null;
        }

        return authUser;
    }

    @Override
    public TokenSet rotateRefreshToken(String oldToken) {
        var authUser = authenticateRefreshToken(oldToken);
        var refreshToken = generateRefreshToken(authUser.handle(), authUser.email(), authUser.role());

        var res = refreshTokenService.rotateRefreshToken(refreshToken, oldToken);
        if (!res) {
            return null;
        }

        return TokenSet.builder()
                .accessToken(generateRefreshToken(authUser.handle(), authUser.email(), authUser.role()))
                .refreshToken(refreshToken)
                .build();
    }

    private String generateAccessToken(String userName, String email, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuer(issuer)
                .subject(userName)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(Long.parseLong(accessTokenExpirationSeconds))))
                .claim("email", email)
                .claim("role", role)
                .signWith(accessKey)
                .compact();
    }

    private String generateRefreshToken(String userName, String email, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuer(issuer)
                .subject(userName)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(Long.parseLong(refreshTokenExpirationSeconds))))
                .claim("email", email)
                .claim("role", role)
                .signWith(refreshKey)
                .compact();
    }

    private AuthUser authenticateRefreshToken(String token) {
        return authenticateToken(token, refreshKey);
    }

    private AuthUser authenticateToken(String token, SecretKey secretKey) {
        try {
            var payload = validateJwtToken(token, secretKey).getPayload();

            return AuthUser.builder()
                    .handle(payload.getSubject())
                    .email(payload.get("email", String.class))
                    .role(payload.get("role", String.class))
                    .build();
        } catch (JwtException e) {
            log.error("\nInvalid jwt: {}. Error: {}", token, e.toString());
            throw new ServiceException("Invalid token", HttpStatus.UNAUTHORIZED.value());
        }
    }

    private Jws<Claims> validateJwtToken(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}

