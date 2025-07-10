package org.example.api.resource;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.example.error.ServiceException;
import org.springframework.stereotype.Controller;

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

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response throwException() throws ServiceException {
        throw new ServiceException("Some exception", Response.Status.BAD_REQUEST.getStatusCode());
    }
}
