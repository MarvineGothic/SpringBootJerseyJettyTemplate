package org.example.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Role;
import org.example.model.ValidEmail;
import org.springframework.lang.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
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
    private Role role;
}
