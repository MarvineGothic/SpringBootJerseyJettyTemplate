package org.example.authentication;

import lombok.Builder;

@Builder
public record AuthenticatedUser(String handle, String email, String role) {
}
