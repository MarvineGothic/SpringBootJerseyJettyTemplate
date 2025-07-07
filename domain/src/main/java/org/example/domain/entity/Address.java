package org.example.domain.entity;

import lombok.*;
import org.example.domain.valueobject.PostCode;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private Long id;

    private String house;

    private String floor;

    private String street;

    private PostCode postCode;

    private String city;

    private String country;

    public String getPostCode() {
        return postCode.postCode();
    }
}
