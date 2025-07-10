package org.example.authentication;

import lombok.Builder;

@Builder
public record AuthUser(String handle, String email, String role) {
}
