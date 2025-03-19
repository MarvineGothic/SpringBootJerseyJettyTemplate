package org.example.model.authentication;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.example.model.error.ResponseError;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.StringTokenizer;

@Provider // Registers this as a JAX-RS filter
@Authenticated // Binds this filter to methods annotated with @Authenticated
@Priority(Priorities.AUTHENTICATION) // Ensures it runs early in the request pipeline
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo; // Provides access to the method being called

    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        // Check if the annotation is present on the method or class
        if (method.isAnnotationPresent(Authenticated.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(Authenticated.class)) {

            // Simulated authentication logic
            String authHeader = requestContext.getHeaderString("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                requestContext.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity(getUnauthorizedError("Unauthorized: Missing or invalid token"))
                        .build()
                );
            } else {
                // Extract token and validate (dummy logic for example)
                String token = authHeader.substring(7);
                if (!validateToken(token)) {
                    requestContext.abortWith(Response
                            .status(Response.Status.UNAUTHORIZED)
                            .entity(getUnauthorizedError("Unauthorized: Invalid token"))
                            .build()
                    );
                }
            }
        }
    }

    private boolean validateToken(String token) {
        try {
            var decodedToken = new String(Base64.getDecoder().decode(token));
            var tokenizer = new StringTokenizer(decodedToken, ":");
            if (tokenizer.countTokens() != 1) {
                return false;
            }
            var validToken = tokenizer.nextToken();
            return "valid-token".equals(validToken);
        } catch (Exception e) {
            return false;
        }
    }

    private ResponseError getUnauthorizedError(String error) {
        ResponseError responseError = new ResponseError();
        responseError.setHttpStatus(String.valueOf(Response.Status.UNAUTHORIZED.getStatusCode()));
        responseError.setHttpReason(Response.Status.UNAUTHORIZED.getReasonPhrase());
        responseError.setError(error);
        responseError.setPath(uriInfo.getPath());
        responseError.setTimestamp(LocalDateTime.now().toString());
        return responseError;
    }
}
