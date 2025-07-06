package org.example.domain.repository;

import org.example.domain.entity.Address;

import java.util.Optional;

public interface AddressRepository {
    Address save(long userId, Address address);
    Optional<Address> findById(Long id);
}
