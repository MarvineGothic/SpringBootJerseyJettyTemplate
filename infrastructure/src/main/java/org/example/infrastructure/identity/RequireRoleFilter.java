package org.example.infrastructure.identity;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.example.authorization.RequireRole;
import org.example.error.ResponseError;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

@Provider
@Priority(Priorities.AUTHORIZATION + 1)
public class RequireRoleFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        RequireRole annotation = method.getAnnotation(RequireRole.class);

        if (annotation == null) {
            annotation = resourceInfo.getResourceClass().getAnnotation(RequireRole.class);
        }

        if (annotation != null) {
            SecurityContext securityContext = requestContext.getSecurityContext();
            if (securityContext == null || !isAuthenticated(requestContext)) {
                abortContextWithForbidden(requestContext);
                return;
            }

            boolean allowed = Arrays.stream(annotation.value())
                    .anyMatch(accessRole -> securityContext.isUserInRole(accessRole.name()));

            if (!allowed) {
                abortContextWithForbidden(requestContext);
            }
        }
    }

    private static boolean isAuthenticated(ContainerRequestContext requestContext) {
        return requestContext.getSecurityContext().getUserPrincipal() != null;
    }

    private void abortContextWithForbidden(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response
                .status(Response.Status.FORBIDDEN)
                .entity(getForbiddenError())
                .build());
    }

    private ResponseError getForbiddenError() {
        ResponseError responseError = new ResponseError();
        responseError.setHttpStatus(String.valueOf(Response.Status.FORBIDDEN.getStatusCode()));
        responseError.setHttpReason(Response.Status.FORBIDDEN.getReasonPhrase());
        responseError.setError("Forbidden: Insufficient role");
        responseError.setPath(uriInfo.getPath());
        responseError.setTimestamp(LocalDateTime.now().toString());
        return responseError;
    }
}
