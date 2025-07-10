package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.model.AbstractRequestDto;
import org.example.authorization.AccessRole;
import org.example.converter.EnumConverter;
import org.example.validator.emailvalidator.ValidEmail;
import org.example.validator.enumvalidator.ValidEnum;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestModel implements AbstractRequestDto {
    @NotBlank
    @JsonProperty(value = "firstname", required = true)
    private String firstName;

    @NotBlank
    @JsonProperty(value = "lastname", required = true)
    private String lastName;

    @NotBlank(message = "Invalid password")
    @JsonProperty(value = "password", required = true)
    private String password;

    @ValidEmail
    @JsonProperty(value = "email", required = true)
    private String email;

    @JsonProperty(value = "access_role", required = true)
//    @ValidEnum(enumClass = AccessRole.class)
    @EnumConverter
    private AccessRole accessRole;

    @JsonProperty("access_roles")
    @EnumConverter
    private List<AccessRole> accessRoles;

    @JsonProperty("addresses")
    private List<AddressRequestModel> addresses;
}
