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

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private String expirationSeconds;

    @Value("${spring.application.name}")
    private String issuer;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(String userName, String email, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(userName)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(Long.parseLong(expirationSeconds))))
                .claim("email", email)
                .claim("role", role)
                .signWith(signingKey)
                .compact();
    }

    @Override
    public AuthUser authenticate(String token) {
        try {
            var payload = validateToken(token).getPayload();

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

    private Jws<Claims> validateToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token);
    }
}

