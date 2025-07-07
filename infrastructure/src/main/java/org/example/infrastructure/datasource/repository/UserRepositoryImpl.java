package org.example.infrastructure.datasource.repository;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.User;
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
    public User createUser(User user) {
        return UserRepositoryMapper.toDomain(userJpaRepository.save(UserRepositoryMapper.fromDomain(user)));
    }

    @Override
    public List<User> getUsers() {
        return userJpaRepository.findAll().stream().map(UserRepositoryMapper::toDomain).toList();
    }

    @Override
    public Optional<User> getUserByHandle(String handle) {
        return userJpaRepository.findByHandle(handle).map(UserRepositoryMapper::toDomain);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserRepositoryMapper::toDomain);
    }

    @Override
    public Optional<User> getUserByHandleWithAddresses(String handle) {
        return userJpaRepository.findByHandleWithAddresses(handle).map(UserRepositoryMapper::toDomain);
    }
}
