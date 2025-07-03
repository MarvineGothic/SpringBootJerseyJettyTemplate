package org.example.model.response;

import lombok.*;
import org.example.model.AbstractResponseDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseModel extends AbstractResponseDto {
    private String house;

    private String floor;

    private String street;

    private String postCode;

    private String city;

    private String country;
}
