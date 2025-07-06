package org.example.domain.repository;

import org.example.domain.entity.User;

import java.util.List;
import java.util.Optional;

// DataAccess
public interface UserRepository {
    User createUser(User user);
    List<User> getUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByIdWithAddresses(Long id);
}
