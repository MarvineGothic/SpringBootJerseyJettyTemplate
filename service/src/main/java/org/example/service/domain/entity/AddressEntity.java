package org.example.service.domain.entity;

import lombok.*;
import org.example.service.infrastructure.persistence.entity.User;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
    private Long id;

    private String house;

    private String floor;

    private String street;

    private String postCode;

    private String city;

    private String country;

    private List<User> users;
}
