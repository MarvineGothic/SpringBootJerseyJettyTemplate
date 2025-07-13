package org.example.api.resource;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import lombok.RequiredArgsConstructor;
import org.example.application.service.user.UserService;
import org.example.model.request.UserLoginRequestModel;
import org.example.model.response.UserSessionResponseModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "401", description = "Not Authenticated"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "500", description = "Server error")
})
@Controller
@Path("/v1/auth")
@RequiredArgsConstructor
public class AuthResource {
    private final UserService userService;
    private final static String DOMAIN_NAME = "localhost";

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public UserSessionResponseModel userLogin(@RequestBody @Valid @NotNull UserLoginRequestModel userLoginRequestModel,
                                              @Context HttpServletResponse servletResponse) {
        var response = userService.login(userLoginRequestModel);

        NewCookie refreshCookie = new NewCookie("refreshToken", response.getRefreshToken(),
                "/api/v1/auth", DOMAIN_NAME, null, 7 * 24 * 60 * 60, true, true);

        servletResponse.addHeader("Set-Cookie", refreshCookie.toString());
        return response;
    }

    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public UserSessionResponseModel refreshToken(@NotBlank @CookieParam("refreshToken") String refreshToken) {
        return userService.refreshToken(refreshToken);
    }
}
