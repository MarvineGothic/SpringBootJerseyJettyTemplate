package org.example.api.resource;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.example.model.error.ServiceException;
import org.example.model.request.UserRequestDto;
import org.example.service.UserService;
import org.springframework.stereotype.Component;

@Tag(name = "User")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found - The user was not found"),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "500", description = "Server error")
})
@Component
@Path("/v1/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @GET
    @Path("/greeting")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getGreeting() {
        return Response.ok("Hello, world").build();
    }

    @GET
    @Path("/exception")
    @Produces(MediaType.TEXT_PLAIN)
    public Response throwException() throws ServiceException {
        throw new ServiceException("Some exception", Response.Status.BAD_REQUEST.getStatusCode());
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        var users = userService.getUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@NotNull @PathParam("userId") Long userId) {
        var user = userService.getUser(userId);
        return user.map(Response::ok).orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid @NotNull UserRequestDto userRequestDto) throws ServiceException {
        return Response
                .status(Response.Status.CREATED)
                .entity(userService.createUser(userRequestDto)).build();
    }
}
