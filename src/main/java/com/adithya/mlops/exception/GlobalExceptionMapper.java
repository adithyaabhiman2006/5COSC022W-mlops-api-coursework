package com.adithya.mlops.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                500, // Internal Server Error
                "An unexpected error occurred: " + ex.getMessage(),
                System.currentTimeMillis()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}
