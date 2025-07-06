package org.example.infrastructure.datasource.repository;

import org.example.domain.entity.Address;
import org.example.infrastructure.datasource.entity.AddressEntity;

public class AddressRepositoryMapper {

    public static AddressEntity fromDomain(Address address) {
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

    public static Address toDomain(AddressEntity addressEntity) {
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
}
