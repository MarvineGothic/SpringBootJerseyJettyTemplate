package org.example.domain.repository;

import org.example.domain.entity.User;

import java.util.List;
import java.util.Optional;

// DataAccess
public interface UserRepository {
    User createUser(User user);
    List<User> getUsers();
    Optional<User> getUserByHandle(String handle);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByHandleWithAddresses(String handle);
}
