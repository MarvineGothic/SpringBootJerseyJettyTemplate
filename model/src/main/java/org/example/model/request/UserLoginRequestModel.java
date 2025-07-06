package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.model.AbstractRequestDto;
import org.example.validator.emailvalidator.ValidEmail;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestModel implements AbstractRequestDto {
    @NotBlank
    @JsonProperty("password")
    private String password;

    @ValidEmail
    @JsonProperty("email")
    private String email;
}
