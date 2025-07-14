package org.example.infrastructure.datasource.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity(name = "RefreshToken")
@Table(name = "refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken extends JpaEntity {
    @ManyToOne(optional = false)
    private UserEntity user;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked")
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "replaced_by")
    private RefreshToken replacedBy;

    @Transient
    public boolean isExpired() {
        return this.getExpiresAt().isBefore(Instant.now());
    }
}
