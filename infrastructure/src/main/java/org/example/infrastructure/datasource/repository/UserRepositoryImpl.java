package org.example.infrastructure.datasource.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.UserEntity;
import org.example.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserRepositoryImpl implements UserRepository { // DataSource
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity createUser(UserEntity user) {
        return UserRepositoryMapper.toDomain(userJpaRepository.save(UserRepositoryMapper.fromDomain(user)));
    }

    @Override
    public List<UserEntity> getUsers() {
        return userJpaRepository.findAll().stream().map(UserRepositoryMapper::toDomain).toList();
    }

    @Override
    public Optional<UserEntity> getUserById(Long id) {
        return userJpaRepository.findById(id).map(UserRepositoryMapper::toDomain);
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserRepositoryMapper::toDomain);
    }

    @Override
    public Optional<UserEntity> getUserByIdWithAddresses(Long id) {
        return userJpaRepository.findByIdWithAddresses(id).map(UserRepositoryMapper::toDomain);
    }
}
