package org.example.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.authorization.AccessRole;
import org.example.error.ResponseError;
import org.example.model.request.CreateUserRequestModel;
import org.example.model.request.UserLoginRequestModel;
import org.example.model.response.UserResponseModel;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserResourceTest {

    @Inject
    private Flyway flyway;

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "api/v1/users";
    private URI uri;
    private Client client;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        uri = new URI("http://localhost:" + port);
        client = ClientBuilder.newClient();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void getException() {
        Response response = client.target(uri).path(BASE_URL).path("/exception")
                .request(MediaType.TEXT_PLAIN).get();
        assertEquals(400, response.getStatus());
        var serviceError = response.readEntity(ResponseError.class);
        assertEquals("Some exception", serviceError.getError());
    }

    @Test
    public void getUserNotFound() {
        Response response = client.target(uri).path(BASE_URL).path("/s8d7f9as87d9f87as9d8f79sfd")
                .request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());
        var errorResponse = response.readEntity(ResponseError.class);
        assertEquals("User not found", errorResponse.getError());
    }

    @Test
    public void getUsersEmpty() {
        Response response = client.target(uri).path(BASE_URL).path("/list")
                .request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());
        var list = response.readEntity(new GenericType<List<UserResponseModel>>() {
        });
        assertTrue(list.isEmpty());
    }

    @Test
    public void createUserMissingPassword() {
        CreateUserRequestModel createUserRequestModel = CreateUserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe1@gmail.com")
                .accessRole(AccessRole.ROLE_USER)
                .build();

        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(createUserRequestModel))) {
            assertEquals(400, response.getStatus());
            var errorResponse = response.readEntity(ResponseError.class);
            assertTrue(errorResponse.getError().contains("Invalid password"));
        }
    }

    @Test
    public void createUserInvalidEmail() {
        CreateUserRequestModel createUserRequestModel = CreateUserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe2@gmail")
                .password("slfsdfsoidufoi")
                .accessRole(AccessRole.ROLE_USER)
                .build();

        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(createUserRequestModel))) {
            assertEquals(400, response.getStatus());
            var errorResponse = response.readEntity(ResponseError.class);
            assertTrue(errorResponse.getError().contains("Invalid email"));
        }
    }

    @Test
    public void createUserSuccess() {
        CreateUserRequestModel createUserRequestModel = CreateUserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe3@gmail.com")
                .password("slfsdfsoidufoi")
                .accessRole(AccessRole.ROLE_USER)
                .build();

        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(createUserRequestModel))) {
            assertEquals(201, response.getStatus());
            var user = response.readEntity(UserResponseModel.class);
            assertEquals("John", user.getFirstName());
        }
    }

    @Test
    public void createUserExists() {
        CreateUserRequestModel createUserRequestModel = CreateUserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe4@gmail.com")
                .password("slfsdfsoidufoi")
                .accessRole(AccessRole.ROLE_USER)
                .build();

        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(createUserRequestModel))) {
            assertEquals(201, response.getStatus());
            var user = response.readEntity(UserResponseModel.class);
            assertEquals("John", user.getFirstName());
        }
        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(createUserRequestModel))) {
            assertEquals(400, response.getStatus());
            var errorResponse = response.readEntity(ResponseError.class);
            assertEquals("User already exist", errorResponse.getError());
        }
    }

    @Test
    public void userLoginUserNotFound() {
        var request = UserLoginRequestModel.builder()
                .email("johndoe5@gmail.com")
                .password("userpassword")
                .build();
        ResponseError errorResponse;
        try (Response response = client.target(uri).path(BASE_URL).path("/login")
                .request(MediaType.APPLICATION_JSON).post(Entity.json(request))) {
            assertEquals(400, response.getStatus());
            errorResponse = response.readEntity(ResponseError.class);
            assertEquals("Bad credentials", errorResponse.getError());
        }
    }

    @Test
    public void userLoginUserSuccess() {
        CreateUserRequestModel createUserRequestModel = CreateUserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe6@gmail.com")
                .password("userpassword")
                .accessRole(AccessRole.ROLE_USER)
                .build();

        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(createUserRequestModel))) {
            assertEquals(201, response.getStatus());
            var user = response.readEntity(UserResponseModel.class);
            assertEquals("John", user.getFirstName());
        }

        var loginRequestModel = UserLoginRequestModel.builder()
                .email("johndoe6@gmail.com")
                .password("userpassword")
                .build();
        try (Response response = client.target(uri).path(BASE_URL).path("/login")
                .request(MediaType.APPLICATION_JSON).post(Entity.json(loginRequestModel))) {
            assertEquals(201, response.getStatus());
        }
    }
}
