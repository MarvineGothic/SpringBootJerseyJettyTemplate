package org.example.application.service.service;

import org.example.domain.entity.AddressEntity;
import org.example.infrastructure.persistence.entity.Address;

public class AddressRepositoryGateway {

    public static Address fromDomain(AddressEntity addressEntity) {
        return Address.builder()
                .id(addressEntity.getId())
                .house(addressEntity.getHouse())
                .floor(addressEntity.getFloor())
                .street(addressEntity.getStreet())
                .postCode(addressEntity.getPostCode())
                .city(addressEntity.getCity())
                .country(addressEntity.getCountry())
                .build();
    }

    public static AddressEntity toDomain(Address address) {
        return AddressEntity.builder()
                .id(address.getId())
                .house(address.getHouse())
                .floor(address.getFloor())
                .street(address.getStreet())
                .postCode(address.getPostCode())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }
}
