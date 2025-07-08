package org.example.infrastructure.datasource.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.authorization.AccessRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserEntity extends JpaEntity {

    @Column(name = "handle", unique = true, updatable = false, nullable = false)
    private String handle;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "access_role")
    @Enumerated(value = EnumType.STRING)
    private AccessRole accessRole;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_addresses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    @Setter
    private List<AddressEntity> addresses = new ArrayList<>();
}
