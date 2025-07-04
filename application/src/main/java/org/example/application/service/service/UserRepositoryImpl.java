package org.example.application.service.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.UserEntity;
import org.example.domain.repository.UserRepository;
import org.example.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity save(UserEntity user) {
        return UserRepositoryGateway.toDomain(userJpaRepository.save(UserRepositoryGateway.fromDomain(user)));
    }

    @Override
    public List<UserEntity> findAll() {
        return userJpaRepository.findAll().stream().map(UserRepositoryGateway::toDomain).toList();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userJpaRepository.findById(id).map(UserRepositoryGateway::toDomain);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserRepositoryGateway::toDomain);
    }

    @Override
    public Optional<UserEntity> findByIdWithAddresses(Long id) {
        return userJpaRepository.findByIdWithAddresses(id).map(UserRepositoryGateway::toDomain);
    }
}
