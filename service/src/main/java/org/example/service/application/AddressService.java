package org.example.service.application;

import lombok.RequiredArgsConstructor;
import org.example.service.infrastructure.persistence.entity.Address;
import org.example.service.infrastructure.persistence.repository.AddressJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressJpaRepository addressJpaRepository;

    public Address addAddress(Address address) {
        return addressJpaRepository.save(address);
    }
}
