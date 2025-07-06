package org.example.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.error.ResponseError;
import org.example.infrastructure.datasource.repository.AddressJpaRepository;
import org.example.infrastructure.datasource.repository.UserJpaRepository;
import org.example.model.request.UserRequestModel;
import org.example.model.response.UserResponseModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressEntityResourceTest {

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "api/v1/users";
    private URI uri;
    private Client client;

    @Inject
    private UserJpaRepository userJpaRepository;
    @Inject
    private AddressJpaRepository addressJpaRepository;

    @Before
    public void setUp() throws URISyntaxException {
        uri = new URI("http://localhost:" + port);
        client = ClientBuilder.newClient();
    }

    @Test
    public void getException() {
        Response response = client.target(uri).path(BASE_URL).path("/exception")
                .request(MediaType.TEXT_PLAIN).get();
        assertEquals(400, response.getStatus());
        var serviceError = response.readEntity(ResponseError.class);
        assertEquals("Some exception", serviceError.getError());
        assertEquals(400, serviceError.getCode());
    }

    @Test
    public void testGreeting() {
        Response response = client.target(uri).path(BASE_URL).path("/greeting")
                .request(MediaType.TEXT_PLAIN).get();
        assertEquals(200, response.getStatus());
        String entity = response.readEntity(String.class);
        assertEquals("Hello, world", entity);
    }

    @Test
    public void getUserNotFound() {
        Response response = client.target(uri).path(BASE_URL).path("/" + 1L)
                .request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());
//        var errorResponse = response.readEntity(ResponseError.class);
//        assertEquals("Hello, world", errorResponse.getErrorMessage());
    }

    @Test
    public void getUsersEmpty() {
        Response response = client.target(uri).path(BASE_URL).path("/list")
                .request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());
        var list = response.readEntity(new GenericType<List<UserResponseModel>>(){});
        assertTrue(list.isEmpty());
    }

    @Test
    public void createUserMissingPassword() {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .build();

        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(userRequestModel))) {
            assertEquals(400, response.getStatus());
        }
    }

    @Test
    public void createUserInvalidEmail() {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail")
                .build();

        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(userRequestModel))) {
            assertEquals(400, response.getStatus());
        }
    }

    @Test
    public void createUserSuccess() {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .password("slfsdfsoidufoi")
                .build();

        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(userRequestModel))) {
            assertEquals(201, response.getStatus());
            var user = response.readEntity(UserResponseModel.class);
            assertEquals("John", user.getFirstName());
        }
        addressJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @Test
    public void createUserExists() {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .password("slfsdfsoidufoi")
                .build();

        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(userRequestModel))) {
            assertEquals(201, response.getStatus());
            var user = response.readEntity(UserResponseModel.class);
            assertEquals("John", user.getFirstName());
        }
        try (Response response = client.target(uri).path(BASE_URL)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(userRequestModel))) {
            assertEquals(400, response.getStatus());
        }
        addressJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }
}
