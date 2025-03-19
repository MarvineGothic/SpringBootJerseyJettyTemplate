package org.example.model.error;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

/**
 * Maps exceptions to jakarta Response
 */
public class ServiceExceptionMapper implements ExceptionMapper<ServiceException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ServiceException e) {
        var serviceError = new ResponseError();
        serviceError.setCode(e.getStatusCode());
        serviceError.setError(e.getMessage());
        serviceError.setHttpStatus(String.valueOf(e.getStatusCode()));
        serviceError.setPath(uriInfo.getPath());
        serviceError.setTimestamp(LocalDateTime.now().toString());

        e.printStackTrace(new PrintWriter(new StringWriter()));
        return Response.status(e.getStatusCode())
                .entity(serviceError)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
