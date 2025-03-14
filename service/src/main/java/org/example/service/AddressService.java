package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.Address;
import org.example.database.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }
}
