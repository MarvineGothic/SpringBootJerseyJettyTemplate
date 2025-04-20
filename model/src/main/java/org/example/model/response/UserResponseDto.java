package org.example.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.model.AbstractResponseDto;
import org.example.authorization.AccessRole;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto extends AbstractResponseDto {
    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("lastname")
    private String lastName;

    private String email;

    @JsonProperty("access_role")
    private AccessRole accessRole;
}
