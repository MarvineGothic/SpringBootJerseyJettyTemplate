package org.example.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.security.auth.Subject;
import java.security.Principal;

@Getter
@Setter
public class UserPrincipal implements Principal {
    private String name;

    @JsonProperty("authenticated_user")
    private AuthUser authUser;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
