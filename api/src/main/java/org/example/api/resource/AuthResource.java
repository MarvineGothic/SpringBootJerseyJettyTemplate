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
import lombok.RequiredArgsConstructor;
import org.example.application.service.user.UserService;
import org.example.error.ServiceException;
import org.example.model.request.UserLoginRequestModel;
import org.example.model.response.UserSessionResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;

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
    private final static String REFRESH_TOKEN = "refresh_token";

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public UserSessionResponseModel userLogin(@RequestBody @Valid @NotNull UserLoginRequestModel userLoginRequestModel,
                                              @Context HttpServletResponse servletResponse) {
        var response = userService.login(userLoginRequestModel);

        setRefreshTokenCookie(servletResponse, response.getRefreshToken());

        return response;
    }

    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public UserSessionResponseModel refreshToken(@NotBlank @CookieParam(REFRESH_TOKEN) String refreshToken,
                                                 @Context HttpServletResponse servletResponse) {
        var responseModel = userService.refreshToken(refreshToken);
        if (responseModel == null) {
            removeRefreshTokenCookie(servletResponse);
            throw new ServiceException("Invalid refresh token", HttpStatus.UNAUTHORIZED.value());
        }

        setRefreshTokenCookie(servletResponse, responseModel.getRefreshToken());

        return responseModel;
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .domain(DOMAIN_NAME)
                .maxAge(Duration.ofDays(7))
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    private void removeRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth") // MUST match the original cookie path
                .maxAge(0)     // Remove immediately
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }
}
