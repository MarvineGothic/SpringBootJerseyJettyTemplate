package org.example.domain.repository;

import org.example.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

// DataAccess
public interface UserRepository {
    UserEntity createUser(UserEntity user);
    List<UserEntity> getUsers();
    Optional<UserEntity> getUserById(Long id);
    Optional<UserEntity> getUserByEmail(String email);
    Optional<UserEntity> getUserByIdWithAddresses(Long id);
}
