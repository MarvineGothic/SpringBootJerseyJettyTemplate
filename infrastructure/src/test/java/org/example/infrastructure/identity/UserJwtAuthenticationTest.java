package org.example.infrastructure.identity;

import org.example.infrastructure.InfrastructureTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

//import static org.junit.Assert.assertNull;

//@RunWith(SpringRunner.class)
@SpringBootTest(classes = InfrastructureTestConfiguration.class)
//@ActiveProfiles("test")
public class UserJwtAuthenticationTest {

//    @Inject
//    private UserJwtAuthenticationService userJwtAuthenticationService;

    @Test
    public void tokenExpired() {
        var expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMWp6dG5wZ2h3ODZ3dzYydDdianpnbmJ0aiIsImlhdCI6MTc1MjE2ODA5MywiZXhwIjoxNzUyMTcxNjkzLCJlbWFpbCI6ImpvaG5kb2UxQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIifQ.dZG_PJIF3TZERJs-msvdz3KU6G-DaTf7ueB22J1T2bw";
//        var authUser = userJwtAuthenticationService.authenticate(expiredToken);
//        assertNull(authUser);
    }
}
