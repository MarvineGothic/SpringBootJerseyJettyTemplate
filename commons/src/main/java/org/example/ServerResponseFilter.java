package org.example;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.example.error.ResponseError;

import java.time.LocalDateTime;

@Provider
public class ServerResponseFilter implements ContainerResponseFilter {

    @Context
    UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        var method = requestContext.getMethod();
        var entity = responseContext.getEntity();
        var code = responseContext.getStatusInfo().getStatusCode();
        var reason = responseContext.getStatusInfo().getReasonPhrase();

        if (method.equalsIgnoreCase("POST") && code == 200) {
            responseContext.setStatusInfo(Response.Status.CREATED);
        }
        if (code >= 300 && entity instanceof String) {
            var responseError = new ResponseError();
            responseError.setCode(code);
            responseError.setError((String) entity);
            responseError.setHttpStatus(String.valueOf(code));
            responseError.setHttpReason(reason);
            responseError.setPath(uriInfo.getPath());
            responseError.setTimestamp(LocalDateTime.now().toString());

            responseContext.setEntity(responseError, responseContext.getEntityAnnotations(), MediaType.APPLICATION_JSON_TYPE);
        }
    }
}
