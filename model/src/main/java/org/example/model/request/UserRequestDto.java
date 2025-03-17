package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.example.model.AbstractRequestDto;
import org.example.model.Role;
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

    @Null
    private Role role;
}
