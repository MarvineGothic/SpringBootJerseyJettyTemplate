package org.example.service.infrastructure.persistence.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.service.domain.entity.UserEntity;
import org.example.service.domain.repository.UserRepository;
import org.example.service.infrastructure.persistence.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save((User) user);
    }

    @Override
    public List<UserEntity> findAll() {
        return userJpaRepository.findAll().stream().map(user -> (UserEntity) user).toList();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userJpaRepository.findById(id).map(user -> user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(user -> user);
    }

    @Override
    public Optional<UserEntity> findByIdWithAddresses(Long id) {
        return userJpaRepository.findByIdWithAddresses(id).map(user -> user);
    }
}
