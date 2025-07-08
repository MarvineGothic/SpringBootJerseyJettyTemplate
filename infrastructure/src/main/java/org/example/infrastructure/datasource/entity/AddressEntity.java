package org.example.infrastructure.datasource.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "address")
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AddressEntity extends JpaEntity {

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

    @Setter
    @ManyToMany(mappedBy = "addresses")
    private List<UserEntity> users = new ArrayList<>();
}
