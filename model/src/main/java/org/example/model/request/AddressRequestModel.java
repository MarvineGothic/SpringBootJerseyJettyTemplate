package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.model.AbstractRequestDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestModel implements AbstractRequestDto {
    @NotBlank
    @JsonProperty("house")
    private String house;

    @JsonProperty("floor")
    private String floor;

    @NotBlank
    @JsonProperty("street")
    private String street;

    @NotBlank
    @JsonProperty("postcode")
    private String postCode;

    @NotBlank
    @JsonProperty("city")
    private String city;

    @NotBlank
    @JsonProperty("country")
    private String country;
}
