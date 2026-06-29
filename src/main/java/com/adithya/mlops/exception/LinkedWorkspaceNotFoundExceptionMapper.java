package com.adithya.mlops.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LinkedWorkspaceNotFoundExceptionMapper implements ExceptionMapper<LinkedWorkspaceNotFoundException> {
    @Override
    public Response toResponse(LinkedWorkspaceNotFoundException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                422, // Unprocessable Entity
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return Response.status(422)
                .entity(errorResponse)
                .build();
    }
}
