package org.example.domain.valueobject;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
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
