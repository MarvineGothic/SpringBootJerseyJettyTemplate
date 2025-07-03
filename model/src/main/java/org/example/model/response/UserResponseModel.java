package org.example.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.model.AbstractResponseDto;
import org.example.authorization.AccessRole;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseModel extends AbstractResponseDto {
    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("lastname")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("access_role")
    private AccessRole accessRole;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("addresses")
    private Set<AddressResponseModel> addresses;
}
