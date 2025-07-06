package org.example.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.model.AbstractResponseDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionResponseModel extends AbstractResponseDto {
    @JsonProperty("session_token")
    String sessionToken;
}
