package org.example.domain.entity;

import lombok.*;

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
}
