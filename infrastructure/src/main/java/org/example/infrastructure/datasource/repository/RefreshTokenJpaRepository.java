package org.example.infrastructure.datasource.repository;

import org.example.infrastructure.datasource.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserHandleAndRevokedIsFalse(String userHandle);
    List<RefreshToken> findAllByUserHandle(String userHandle);
}
