package org.example.service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.authorization.AccessRole;
import org.example.service.infrastructure.persistence.entity.Address;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
//@Builder
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

    private List<Address> addresses;
}
