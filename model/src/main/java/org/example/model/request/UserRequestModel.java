package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.model.AbstractRequestDto;
import org.example.authorization.AccessRole;
import org.example.converter.EnumConverter;
import org.example.validator.emailvalidator.ValidEmail;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestModel implements AbstractRequestDto {
    @NotBlank
    @JsonProperty("firstname")
    private String firstName;

    @NotBlank
    @JsonProperty("lastname")
    private String lastName;

    @NotBlank
    @JsonProperty("password")
    private String password;

    @ValidEmail
    @JsonProperty("email")
    private String email;

    @Nullable
    @JsonProperty("access_role")
//    @ValidEnum(enumClass = AccessRole.class)
    @EnumConverter
    private AccessRole accessRole;

    @JsonProperty("access_roles")
    @EnumConverter
    private List<AccessRole> accessRoles;
}
