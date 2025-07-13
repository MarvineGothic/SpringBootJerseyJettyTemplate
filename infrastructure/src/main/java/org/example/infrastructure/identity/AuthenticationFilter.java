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
import org.example.authentication.*;
import org.example.authentication.annotation.Authenticated;
import org.example.error.ResponseError;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Provider // Registers this as a JAX-RS filter
@Authenticated // Binds this filter to methods annotated with @Authenticated
@Priority(Priorities.AUTHENTICATION) // Ensures it runs early in the request pipeline
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final String BASIC_AUTH_PREFIX = "Basic ";
    private static final String BEARER_AUTH_PREFIX = "Bearer ";

    @Inject
    private BasicAuthenticationService basicAuthenticationService;

    @Inject
    private JwtAuthenticationService jwtAuthenticationService;

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

            AuthUser authUser = null;

            String authHeader = requestContext.getHeaderString("Authorization");
            if (authHeader == null || authHeader.isBlank()) {
                abortContextWithUnauthorized(requestContext);
                return;
            } else if (authHeader.startsWith(BASIC_AUTH_PREFIX)) { // Basic is deprecating. Use JWT or OAUTH
                String token = authHeader.replace(BASIC_AUTH_PREFIX, "");
                authUser = basicAuthenticationService.authenticate(token);
            } else if (authHeader.startsWith(BEARER_AUTH_PREFIX)) {
                String token = authHeader.replace(BEARER_AUTH_PREFIX, "");
                authUser = jwtAuthenticationService.authenticateAccessToken(token);
            }

            if (authUser == null) {
                abortContextWithUnauthorized(requestContext);
                return;
            }

            setSecurityContext(requestContext, authUser);
        }
    }

    private void setSecurityContext(ContainerRequestContext requestContext, AuthUser authUser) {
        var userPrincipal = new UserPrincipal();
        userPrincipal.setName(authUser.handle());
        userPrincipal.setAuthUser(authUser);

        var userSecurityContext = new UserSecurityContext();
        userSecurityContext.setUserRole(authUser.role());
        userSecurityContext.setUserPrincipal(userPrincipal);
        userSecurityContext.setSchema(requestContext.getUriInfo().getRequestUri().getScheme());

        requestContext.setSecurityContext(userSecurityContext);
    }

    private void abortContextWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(getUnauthorizedError())
                .build());
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
