package org.example.domain.entity;

import lombok.*;
import org.example.authorization.AccessRole;
import org.example.domain.valueobject.Address;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String handle;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private AccessRole accessRole;

    private LocalDateTime creationTime;

    private List<Address> addresses = new ArrayList<>();
}
