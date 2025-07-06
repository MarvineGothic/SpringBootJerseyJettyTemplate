package org.example.infrastructure.identity;

import lombok.RequiredArgsConstructor;
import org.example.authentication.BasicAuthenticationService;
import org.example.authentication.UserPrincipal;
import org.example.authentication.UserSecurityContext;
import org.example.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Objects;
import java.util.StringTokenizer;

@RequiredArgsConstructor
@Service
public class UserBasicAuthenticationService implements BasicAuthenticationService {
    private final UserRepository userRepository;

    @Override
    public String getBasicAuthenticationToken(String username, String password) {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    @Override
    public UserSecurityContext validateBasicAuthenticationToken(String token) {
        try {
            var decodedToken = new String(Base64.getDecoder().decode(token));
            var tokenizer = new StringTokenizer(decodedToken, ":");
            if (tokenizer.countTokens() != 2) {
                return null;
            }
            var email = tokenizer.nextToken();
            var password = tokenizer.nextToken();
            var user = userRepository.getUserByEmail(email);
            if (user.isEmpty()) {
                return null;
            } else if (password == null || !Objects.equals(user.get().getPassword(), password)){
                return null;
            }
            var userPrincipal = new UserPrincipal();
            userPrincipal.setUsername(email);

            var userSecurityContext = new UserSecurityContext();
            userSecurityContext.setUserRole(user.get().getAccessRole().name());
            userSecurityContext.setUserPrincipal(userPrincipal);

            return userSecurityContext;
        } catch (Exception e) {
            return null;
        }
    }
}
