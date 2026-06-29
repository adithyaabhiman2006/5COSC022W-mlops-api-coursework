package com.adithya.mlops.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WorkspaceNotEmptyExceptionMapper implements ExceptionMapper<WorkspaceNotEmptyException> {
    @Override
    public Response toResponse(WorkspaceNotEmptyException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                409, // Conflict
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return Response.status(Response.Status.CONFLICT)
                .entity(errorResponse)
                .build();
    }
}
