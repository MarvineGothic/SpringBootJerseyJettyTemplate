package org.example.domain.entity;

import lombok.*;
import org.example.authorization.AccessRole;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private AccessRole accessRole;

    private LocalDateTime creationTime;
}
