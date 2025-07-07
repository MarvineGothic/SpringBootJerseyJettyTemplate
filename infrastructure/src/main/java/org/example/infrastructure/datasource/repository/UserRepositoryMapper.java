package org.example.infrastructure.datasource.repository;

import org.example.domain.entity.User;
import org.example.infrastructure.datasource.entity.UserEntity;

import java.util.List;

public class UserRepositoryMapper { // EntityMapper

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
//                .id(null)
                .handle(user.getHandle())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .accessRole(user.getAccessRole())
                .creationTime(user.getCreationTime())
                .addresses(List.of())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId()) // delete
                .handle(userEntity.getHandle())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .accessRole(userEntity.getAccessRole())
                .creationTime(userEntity.getCreationTime())
                .build();
    }
}
