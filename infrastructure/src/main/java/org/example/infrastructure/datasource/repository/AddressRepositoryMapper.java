package org.example.infrastructure.datasource.repository;

import org.example.domain.valueobject.Address;
import org.example.domain.valueobject.PostCode;
import org.example.infrastructure.datasource.entity.AddressEntity;

public class AddressRepositoryMapper {

    public static AddressEntity fromDomain(Address address) {
        return AddressEntity.builder()
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
                .house(addressEntity.getHouse())
                .floor(addressEntity.getFloor())
                .street(addressEntity.getStreet())
                .postCode(new PostCode(addressEntity.getPostCode()))
                .city(addressEntity.getCity())
                .country(addressEntity.getCountry())
                .build();
    }
}
