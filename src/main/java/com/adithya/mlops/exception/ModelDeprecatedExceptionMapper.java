package com.adithya.mlops.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ModelDeprecatedExceptionMapper implements ExceptionMapper<ModelDeprecatedException> {
    @Override
    public Response toResponse(ModelDeprecatedException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                403, // Forbidden
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return Response.status(Response.Status.FORBIDDEN)
                .entity(errorResponse)
                .build();
    }
}
