package org.example.domain.repository;

import org.example.domain.entity.Address;

import java.util.Optional;

public interface AddressRepository {
    Address save(String handle, Address address);
}
