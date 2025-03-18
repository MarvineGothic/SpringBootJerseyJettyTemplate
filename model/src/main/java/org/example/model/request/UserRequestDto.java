package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.model.AbstractRequestDto;
import org.example.model.authorization.AccessRole;
import org.example.model.converter.AccessRoleConverter;
import org.example.validator.emailvalidator.ValidEmail;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto extends AbstractRequestDto {
    @NotBlank
    @JsonProperty("firstname")
    private String firstName;

    @NotBlank
    @JsonProperty("lastname")
    private String lastName;

    @NotBlank
    private String password;

    @ValidEmail
    private String email;

    @Nullable
    @JsonProperty("access_role")
//    @ValidEnum(enumClass = AccessRole.class)
//    @JsonDeserialize(using = AccessRoleDeserializer.class)
    @AccessRoleConverter
    private AccessRole accessRole;
}
