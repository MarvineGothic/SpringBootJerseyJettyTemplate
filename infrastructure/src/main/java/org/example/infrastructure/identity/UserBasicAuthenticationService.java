package org.example.infrastructure.identity;

import lombok.RequiredArgsConstructor;
import org.example.authentication.AuthenticatedUser;
import org.example.authentication.BasicAuthenticationService;
import org.example.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringTokenizer;

@RequiredArgsConstructor
@Service
public class UserBasicAuthenticationService implements BasicAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String generateToken(String username, String password) {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    @Override
    public AuthenticatedUser authenticate(String token) {
        return validateToken(token);
    }

    private AuthenticatedUser validateToken(String token) {
        try {
            var decodedToken = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
            var tokenizer = new StringTokenizer(decodedToken, ":");
            if (tokenizer.countTokens() != 2) {
                return null;
            }
            var email = tokenizer.nextToken();
            var password = tokenizer.nextToken();
            return userRepository.getUserByEmail(email)
                    .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                    .map(u -> AuthenticatedUser.builder()
                            .handle(u.getHandle())
                            .email(u.getEmail())
                            .role(u.getAccessRole().name())
                            .build()
                    ).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
