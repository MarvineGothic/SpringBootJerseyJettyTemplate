package org.example.infrastructure.datasource.repository;

import org.example.domain.entity.User;
import org.example.infrastructure.datasource.entity.UserEntity;

import java.util.List;

public class UserRepositoryMapper { // EntityMapper

    public static UserEntity fromDomain(User addressEntity) {
        return UserEntity.builder()
                .id(addressEntity.getId())
                .firstName(addressEntity.getFirstName())
                .lastName(addressEntity.getLastName())
                .email(addressEntity.getEmail())
                .password(addressEntity.getPassword())
                .accessRole(addressEntity.getAccessRole())
                .creationTime(addressEntity.getCreationTime())
                .addresses(List.of())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .accessRole(userEntity.getAccessRole())
                .creationTime(userEntity.getCreationTime())
                .build();
    }
}
