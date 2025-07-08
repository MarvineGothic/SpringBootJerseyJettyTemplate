package org.example.infrastructure.datasource.repository;

import org.example.domain.entity.User;
import org.example.infrastructure.datasource.entity.UserEntity;

import java.util.List;

public class UserRepositoryMapper { // EntityMapper

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .handle(user.getHandle())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .accessRole(user.getAccessRole())
                .creationTime(user.getCreationTime())
                .addresses(user.getAddresses().stream().map(AddressRepositoryMapper::fromDomain).toList())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.builder()
                .handle(userEntity.getHandle())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .accessRole(userEntity.getAccessRole())
                .creationTime(userEntity.getCreationTime())
                .addresses(userEntity.getAddresses().stream().map(AddressRepositoryMapper::toDomain).toList())
                .build();
    }
}
