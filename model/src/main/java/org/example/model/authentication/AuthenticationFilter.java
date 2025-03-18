package org.example.model.authentication;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.example.model.error.ResponseError;

import java.io.IOException;
import java.lang.reflect.Method;

@Provider // Registers this as a JAX-RS filter
@Authenticated // Binds this filter to methods annotated with @Authenticated
@Priority(1000) // Ensures it runs early in the request pipeline
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo; // Provides access to the method being called

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        // Check if the annotation is present on the method or class
        if (method.isAnnotationPresent(Authenticated.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(Authenticated.class)) {

            // Simulated authentication logic
            String authHeader = requestContext.getHeaderString("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                ResponseError responseError = new ResponseError();
                responseError.setHttpStatus(String.valueOf(Response.Status.UNAUTHORIZED.getStatusCode()));
                responseError.setHttpReason(Response.Status.UNAUTHORIZED.getReasonPhrase());
                responseError.setError("Unauthorized: Missing or invalid token");
                requestContext.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity(responseError)
                        .build()
                );
            } else {
                // Extract token and validate (dummy logic for example)
                String token = authHeader.substring(7);
                if (!validateToken(token)) {
                    ResponseError responseError = new ResponseError();
                    responseError.setHttpStatus(String.valueOf(Response.Status.UNAUTHORIZED.getStatusCode()));
                    responseError.setHttpReason(Response.Status.UNAUTHORIZED.getReasonPhrase());
                    responseError.setError("Unauthorized: Invalid token");
                    requestContext.abortWith(Response
                            .status(Response.Status.UNAUTHORIZED)
                            .entity(responseError)
                            .build()
                    );
                }
            }
        }
    }

    private boolean validateToken(String token) {
        // Dummy token validation - replace with real logic
        return "valid-token".equals(token);
    }
}
