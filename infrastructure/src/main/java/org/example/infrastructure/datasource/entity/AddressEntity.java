package org.example.infrastructure.datasource.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Address")
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddressEntity implements Serializable {
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
    private List<UserEntity> users = new ArrayList<>();
}
