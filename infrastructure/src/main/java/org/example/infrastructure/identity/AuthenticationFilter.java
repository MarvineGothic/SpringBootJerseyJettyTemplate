package org.example.infrastructure.identity;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.apache.commons.lang3.NotImplementedException;
import org.example.authentication.Authenticated;
import org.example.authentication.BasicAuthenticationService;
import org.example.error.ResponseError;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Provider // Registers this as a JAX-RS filter
@Authenticated // Binds this filter to methods annotated with @Authenticated
@Priority(Priorities.AUTHENTICATION) // Ensures it runs early in the request pipeline
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private BasicAuthenticationService basicAuthenticationService;

    @Context
    private SecurityContext securityContext;

    @Context
    private ResourceInfo resourceInfo; // Provides access to the method being called

    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();

        // Check if the annotation is present on the method or class
        if (method.isAnnotationPresent(Authenticated.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(Authenticated.class)) {

            // Simulated authentication logic
            String authHeader = requestContext.getHeaderString("Authorization");
            if (authHeader == null) {
                requestContext.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity(getUnauthorizedError())
                        .build()
                );
            } else if (authHeader.startsWith("Basic ")) { // Basic is deprecating. Use JWT or OAUTH
                String token = authHeader.substring(6);
                var userSecurityContext = basicAuthenticationService.validateBasicAuthenticationToken(token);
                if (userSecurityContext == null) {
                    requestContext.abortWith(Response
                            .status(Response.Status.UNAUTHORIZED)
                            .entity(getUnauthorizedError())
                            .build()
                    );
                }

                requestContext.setSecurityContext(userSecurityContext);
            } else if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                throw new NotImplementedException();
            } else {
                requestContext.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity(getUnauthorizedError())
                        .build());
            }
        }
    }

    private ResponseError getUnauthorizedError() {
        ResponseError responseError = new ResponseError();
        responseError.setHttpStatus(String.valueOf(Response.Status.UNAUTHORIZED.getStatusCode()));
        responseError.setHttpReason(Response.Status.UNAUTHORIZED.getReasonPhrase());
        responseError.setError("Unauthorized: Missing or invalid token");
        responseError.setPath(uriInfo.getPath());
        responseError.setTimestamp(LocalDateTime.now().toString());
        return responseError;
    }
}
