package org.example.error;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

/**
 * Wraps jakarta validation errors to jakarta Response
 */
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        var serviceError = new ResponseError();
        serviceError.setError(e.getMessage());
        serviceError.setCode(Response.Status.BAD_REQUEST.getStatusCode());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(serviceError)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
