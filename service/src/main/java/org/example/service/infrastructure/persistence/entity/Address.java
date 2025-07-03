package org.example.service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.service.domain.entity.AddressEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house")
    private String house;

    @Column(name = "floor")
    private String floor;

    @Column(name = "street")
    private String street;

    @Column(name = "postcode")
    private String postCode;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @ManyToMany(mappedBy = "addresses")
    private Set<User> users = new HashSet<>();

    public static Address fromDomain(AddressEntity addressEntity) {
        return Address.builder()
                .id(addressEntity.getId())
                .house(addressEntity.getHouse())
                .floor(addressEntity.getFloor())
                .street(addressEntity.getStreet())
                .postCode(addressEntity.getPostCode())
                .city(addressEntity.getCity())
                .country(addressEntity.getCountry())
                .users(addressEntity.getUsers())
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
                .users(address.getUsers())
                .build();
    }
}
