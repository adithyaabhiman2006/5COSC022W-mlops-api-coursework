package com.adithya.mlops.exception;

public class WorkspaceNotEmptyException extends RuntimeException {
    public WorkspaceNotEmptyException(String message) {
        super(message);
    }
}
