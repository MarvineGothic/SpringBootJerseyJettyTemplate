package org.example.api.resource;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.example.authentication.Authenticated;
import org.example.error.ServiceException;
import org.example.model.request.CreateUserRequestModel;
import org.example.model.request.UserLoginRequestModel;
import org.example.model.response.AddressResponseModel;
import org.example.model.response.UserResponseModel;
import org.example.application.service.user.UserService;
import org.example.model.response.UserSessionResponseModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@Tag(name = "Users")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found - The user was not found"),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "500", description = "Server error")
})
@Controller
@Path("/v1/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @Context
    private ContainerRequestContext requestContext;

    @GET
    @Path("/exception")
    @Produces(MediaType.TEXT_PLAIN)
    public Response throwException() throws ServiceException {
        throw new ServiceException("Some exception", Response.Status.BAD_REQUEST.getStatusCode());
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserResponseModel> getUsers() {
        return userService.getUsers();
    }

    @GET
    @Path("/{handle}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserResponseModel getUser(@NotBlank @PathParam("handle") String handle) {
        return userService.getUser(handle);
    }

    @GET
    @Path("/{handle}/addresses")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<AddressResponseModel> getUserAddresses(@NotNull @PathParam("handle") String handle) {
        return userService.getUserAddresses(handle);
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public UserSessionResponseModel userLogin(@RequestBody @Valid @NotNull UserLoginRequestModel userLoginRequestModel) {
        return userService.login(userLoginRequestModel);
    }

    @Deprecated
    @POST
    @Path("/login/basic")
    @Produces(MediaType.APPLICATION_JSON)
    public UserSessionResponseModel userLoginBasic(@RequestBody @Valid @NotNull UserLoginRequestModel userLoginRequestModel) {
        return userService.loginBasic(userLoginRequestModel);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserResponseModel createUser(@RequestBody @Valid @NotNull CreateUserRequestModel createUserRequestModel) {
        return userService.createUser(createUserRequestModel);
    }

    @GET
    @Path("/authenticated")
    @Authenticated
    @RolesAllowed(value = {"ROLE_USER"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthenticatedUser() {
        var userPrincipal = requestContext.getSecurityContext().getUserPrincipal();
        return Response.ok(userPrincipal).build();
    }
}
