package org.example.infrastructure.datasource.repository;

import org.example.domain.entity.UserEntity;
import org.example.infrastructure.datasource.entity.User;

import java.util.List;

public class UserRepositoryMapper { // EntityMapper

    public static User fromDomain(UserEntity addressEntity) {
        return User.builder()
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

    public static UserEntity toDomain(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .accessRole(user.getAccessRole())
                .creationTime(user.getCreationTime())
                .build();
    }
}
