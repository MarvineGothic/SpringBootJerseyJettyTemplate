package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUserAddressRequestModel extends AddressRequestModel {
    @NotBlank
    @JsonProperty("user_id")
    private Long userId;
}
