package org.example.authentication;

import java.util.Date;

public record UserJwtPayload(String sub, String email, String role, Date exp, Date iat) {
}
