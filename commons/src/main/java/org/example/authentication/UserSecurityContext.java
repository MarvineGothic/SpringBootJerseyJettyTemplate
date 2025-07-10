package org.example.authentication;

import jakarta.ws.rs.core.SecurityContext;
import lombok.Setter;

import java.security.Principal;

@Setter
public class UserSecurityContext implements SecurityContext {
    public static final String BEARER_AUTH = "BEARER";
    private Principal userPrincipal;
    private String userRole;
    private boolean isSecure;
    private String schema;

    @Override
    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    @Override
    public boolean isUserInRole(String s) {
        return s.equalsIgnoreCase(userRole);
    }

    @Override
    public boolean isSecure() {
//        return "https".equalsIgnoreCase(scheme);
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return BEARER_AUTH;
    }
}
