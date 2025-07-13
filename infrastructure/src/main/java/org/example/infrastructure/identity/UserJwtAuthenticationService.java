package org.example.infrastructure.identity;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserJwtAuthenticationService implements JwtAuthenticationService {
    private final ObjectMapper objectMapper;

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
    public String generateAccessToken(String userName, String email, String role) {
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

    @Override
    public String generateRefreshToken(String userName, String email, String role) {
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

    @Override
    public AuthUser authenticateAccessToken(String token) {
        return authenticate(token, accessKey);
    }

    @Override
    public AuthUser authenticateRefreshToken(String token) {
        return authenticate(token, refreshKey);
    }

    private AuthUser authenticate(String token, SecretKey secretKey) {
        try {
            var payload = validateToken(token, secretKey).getPayload();

            return AuthUser.builder()
                    .handle(payload.getSubject())
                    .email(payload.get("email", String.class))
                    .role(payload.get("role", String.class))
                    .build();
        } catch (JwtException e) {
            log.error("\nInvalid jwt: {}", e.toString());
            return null;
        }
    }

    private Jws<Claims> validateToken(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}

