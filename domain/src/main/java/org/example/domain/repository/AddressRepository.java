package org.example.domain.repository;

import org.example.domain.entity.AddressEntity;

import java.util.Optional;

public interface AddressRepository {
    AddressEntity save(long userId, AddressEntity address);
    Optional<AddressEntity> findById(Long id);
}
