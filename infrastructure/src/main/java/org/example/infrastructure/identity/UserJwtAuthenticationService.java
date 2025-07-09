package org.example.infrastructure.identity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.example.authentication.JwtAuthenticationService;
import org.example.authentication.UserJwtPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class UserJwtAuthenticationService implements JwtAuthenticationService {
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
    public String generateJwt(String userName, String email, String role) {
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
    public UserJwtPayload parseJwt(String jwt) {
        var claims = validateJwt(jwt).getPayload();

        return new UserJwtPayload(
                claims.getSubject(),
                claims.get("email", String.class),
                claims.get("role", String.class),
                claims.getExpiration(),
                claims.getIssuedAt()
        );
    }

    private Jws<Claims> validateJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(jwt);
    }
}

